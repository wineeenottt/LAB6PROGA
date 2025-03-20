package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Класс ClientReader отвечает за чтение объектов CommandContainer из SocketChannel.
 * Используется для десериализации данных, полученных от сервера.
 */
public class ClientReader {
    private static final Logger rootLogger = LoggerFactory.getLogger(ClientReader.class);
    private final SocketChannel clientChannel;
    private static final int BUFFER_SIZE = 4096;
    private final ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
    private ByteBuffer dataBuffer = null;
    private int expectedSize = -1;

    /**
     * Конструктор класса ClientReader.
     *
     * @param clientChannel Канал SocketChannel, используемый для чтения данных от сервера.
     */
    public ClientReader(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
        rootLogger.info("Канал ввода для клиента инициализирован");
    }

    /**
     * Читает объект CommandContainer из SocketChannel.
     * Метод читает размер данных, затем сами данные и десериализует их в объект CommandContainer.
     *
     * @return Возвращает объект CommandContainer, если данные успешно прочитаны и десериализованы.
     * @throws IOException Если возникает ошибка ввода-вывода при чтении данных.
     * @throws ClassNotFoundException Если класс CommandContainer не найден при десериализации.
     */
    public CommandContainer readCommandContainer() throws IOException, ClassNotFoundException {
        if (clientChannel == null || !clientChannel.isOpen()) {
            rootLogger.error("Нет активного соединения с сервером");
            throw new IOException("Нет соединения с сервером");
        }

        if (expectedSize == -1) {
            sizeBuffer.clear();
            int bytesRead = clientChannel.read(sizeBuffer);
            if (bytesRead == -1) {
                rootLogger.error("Соединение закрыто сервером");
                throw new IOException("Соединение закрыто сервером");
            }
            if (bytesRead == 0) {
                rootLogger.debug("Данные размера еще не доступны");
                return null;
            }

            sizeBuffer.flip();
            if (sizeBuffer.remaining() >= 4) {
                expectedSize = sizeBuffer.getInt();
                rootLogger.debug("Получен размер данных: {} байт", expectedSize);
                if (expectedSize <= 0 || expectedSize > BUFFER_SIZE) {
                    rootLogger.error("Некорректный размер данных: {}", expectedSize);
                    throw new IOException("Некорректный размер данных");
                }
                dataBuffer = ByteBuffer.allocate(expectedSize);
                sizeBuffer.clear();
            } else {
                rootLogger.debug("Недостаточно данных для размера, получено: {} байт", bytesRead);
                sizeBuffer.compact();
                return null;
            }
        }

        ByteBuffer tempBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        int bytesRead = clientChannel.read(tempBuffer);
        if (bytesRead == -1) {
            rootLogger.error("Соединение закрыто сервером при чтении данных");
            throw new IOException("Соединение закрыто сервером");
        }
        if (bytesRead > 0) {
            tempBuffer.flip();
            dataBuffer.put(tempBuffer);
            rootLogger.debug("Получено {} из {} байт", dataBuffer.position(), expectedSize);
        }

        if (dataBuffer.position() == expectedSize) {
            dataBuffer.flip();
            byte[] data = new byte[expectedSize];
            dataBuffer.get(data);

            try (ByteArrayInputStream b = new ByteArrayInputStream(data);
                 ObjectInputStream o = new ObjectInputStream(b)) {
                CommandContainer result = (CommandContainer) o.readObject();
                rootLogger.info("Получен CommandContainer: {}, размер: {} байт", result.toString(), expectedSize);

                expectedSize = -1;
                dataBuffer = null;
                return result;
            }
        } else {
            rootLogger.debug("Получено {} из {} байт, ждем остальное", dataBuffer.position(), expectedSize);
            return null;
        }
    }
}