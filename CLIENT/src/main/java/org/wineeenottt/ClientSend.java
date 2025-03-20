package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Класс ClientSend отвечает за отправку объектов CommandContainer на сервер через SocketChannel.
 * Используется для сериализации данных и их передачи по сети.
 */
public class ClientSend {
    private static final Logger rootLogger = LoggerFactory.getLogger(ClientSend.class);
    private final SocketChannel clientChannel;
    private static final int BUFFER_SIZE = 4096;

    /**
     * Конструктор класса ClientSend.
     *
     * @param clientChannel Канал SocketChannel, используемый для отправки данных на сервер.
     */
    public ClientSend(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    /**
     * Отправляет объект CommandContainer на сервер через SocketChannel.
     * Метод сериализует объект, проверяет его размер и отправляет данные по сети.
     *
     * @param command Объект CommandContainer, который необходимо отправить.
     * @return Возвращает true, если отправка прошла успешно, и false в случае ошибки.
     */
    public boolean sendContainer(CommandContainer command) {
        try {
            if (clientChannel == null || !clientChannel.isConnected()) {
                rootLogger.error("Нет активного соединения с сервером");
                return false;
            }

            byte[] serializedData;
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(b)) {
                oos.writeObject(command);
                oos.flush();
                serializedData = b.toByteArray();
            }

            if (serializedData.length > BUFFER_SIZE) {
                rootLogger.error("CommandContainer слишком большой: {} байт", serializedData.length);
                return false;
            }

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.clear();

            buffer.putInt(serializedData.length);
            buffer.put(serializedData);
            buffer.flip();

            while (buffer.hasRemaining()) {
                int bytesWritten = clientChannel.write(buffer);
                if (bytesWritten == -1) {
                    rootLogger.error("Канал закрыт при отправке CommandContainer");
                    return false;
                }
            }

            rootLogger.info("CommandContainer успешно отправлен: {}, размер: {} байт",
                    command.toString(), serializedData.length);
            return true;

        } catch (IOException ex) {
            rootLogger.error("Ошибка при отправке CommandContainer: {}", ex.getMessage());
            return false;
        }
    }
}