package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 * Класс ClientRequest отвечает за чтение объектов CommandContainer из SocketChannel.
 * Используется для десериализации данных, полученных от сервера.
 */
public class ClientRequest {
    /**
     * Логгер для записи.
     */
    private static final Logger rootLogger = LoggerFactory.getLogger(ClientRequest.class);
    /**
     * Канал для связи с сервером.
     */
    private final SocketChannel clientChannel;
    /**
     * Константа - максимальный размер буфера.
     */
    private static final int BUFFER_SIZE = 7000;
    /**
     * Буфер для размера данны.
     */
    private final ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
    /**
     * Буфер для данных.
     */
    private ByteBuffer dataBuffer = null;
    private int expectedSize = -1;

    /**
     * Конструктор класса ClientReader.
     */
    public ClientRequest(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
        rootLogger.info("Канал ввода для клиента инициализирован");
    }

    /**
     * Читает объект CommandContainer из SocketChannel.
     * Метод читает размер данных, затем сами данные и десериализует их в объект CommandContainer.
     */
    public CommandContainer readCommandContainer() throws IOException, ClassNotFoundException {
        //проверяем соединение
        if (clientChannel == null || !clientChannel.isOpen()) {
            rootLogger.error("Нет активного соединения с сервером");
            throw new IOException("Нет соединения с сервером");
        }

        if (expectedSize == -1) { // размер еще не получен
            sizeBuffer.clear();
            int bytesRead = clientChannel.read(sizeBuffer);//пытаемся прочитать размер
            if (bytesRead == -1) {
                rootLogger.error("Соединение закрыто сервером");
                throw new IOException("Соединение закрыто сервером");
            }
            if (bytesRead == 0) {
                //rootLogger.debug("Данные еще не доступны");
                return null;
            }

            sizeBuffer.flip();//позиция 0, только нужные данные
            if (sizeBuffer.remaining() >= 4) { //прочитали весь размер?
                expectedSize = sizeBuffer.getInt(); //извлекаем
                rootLogger.debug("Получен размер данных: {} байт", expectedSize);
                if (expectedSize <= 0 || expectedSize > BUFFER_SIZE) {//коректно?
                    rootLogger.error("Некорректный размер данных: {}", expectedSize);
                    throw new IOException("Некорректный размер данных");
                }
                dataBuffer = ByteBuffer.allocate(expectedSize); //созд буфер нужного размера
                sizeBuffer.clear();//позиция 0
            } else {
                rootLogger.debug("Недостаточно данных, получено: {} байт", bytesRead);
                sizeBuffer.compact();//ждем все данные
                return null;
            }
        }

        ByteBuffer tempBuffer = ByteBuffer.allocate(BUFFER_SIZE); //временный буфер
        int bytesRead = clientChannel.read(tempBuffer);//читаем данные из канала
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