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
 * Класс ClientSend отвечает за отправку объектов CommandContainer на сервер.
 */
public class ClientResponse {
    /**
     * Логгер для записи.
     */
    private static final Logger rootLogger = LoggerFactory.getLogger(ClientResponse.class);
    /**
     * Канал для связи с сервером.
     */
    private final SocketChannel clientChannel;
    /**
     * Константа - максимальный размер буфера.
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Конструктор класса ClientSend.
     */
    public ClientResponse(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    /**
     * Отправляет объект CommandContainer на сервер через SocketChannel.
     * Метод сериализует объект, проверяет его размер и отправляет данные.
     */
    public boolean sendContainer(CommandContainer command) {
        try {
            //проверяем активность соединения
            if (clientChannel == null || !clientChannel.isConnected()) {
                rootLogger.error("Нет активного соединения с сервером");
                return false;
            }

            byte[] serializedData;
            try (ByteArrayOutputStream b = new ByteArrayOutputStream(); //поток для записи байтов в масссив
                 ObjectOutputStream o = new ObjectOutputStream(b)) { //объект сериализации
                o.writeObject(command); //сериализуем
                o.flush();
                serializedData = b.toByteArray(); //массив байтов
            }

            if (serializedData.length > BUFFER_SIZE) {
                rootLogger.error("CommandContainer слишком большой: {} байт", serializedData.length);
                return false;
            }

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.clear();//позиця 0

            buffer.putInt(serializedData.length); //записываем длину
            buffer.put(serializedData);
            buffer.flip();//в буфере только нужные данные

            while (buffer.hasRemaining()) { //остались ли в буфере данные?
                int bytesWritten = clientChannel.write(buffer); //запись в канал
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