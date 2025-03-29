package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;

import java.io.*;
import java.net.Socket;

/**
 * Класс ServerResponse отвечает за отправку данных клиенту через сокет.
 * Он сериализует объекты CommandContainer и отправляет их клиенту.
 */
public class ServerResponse {
    /**
     * Логгер для записи.
     */
    private static final Logger rootLogger = LoggerFactory.getLogger(ServerResponse.class);
    /**
     * Канал для связи с клиентом.
     */
    private final Socket clientSocket;
    /**
     * Поток вывода для отправки данных клиенту.
     */
    private final DataOutputStream dataOutputStream;

    /**
     * Конструктор класса ServerResponse.
     */
    public ServerResponse(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        try {
            this.dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            rootLogger.info("Поток вывода для клиента инициализирован");
        } catch (IOException ex) {
            rootLogger.error("Ошибка при создании потока вывода: {}", ex.getMessage());
            throw ex;
        }
    }

    /**
     * Отправляет объект клиенту.
     * Объект сериализуется и отправляется.
     */
    public void sendCommandContainer(CommandContainer command) {
        try {
            //проверяем активность соединения
            if (clientSocket == null || clientSocket.isClosed()) {
                rootLogger.error("Нет активного соединения с клиентом");
                return;
            }

            ByteArrayOutputStream b = new ByteArrayOutputStream(); //буфер для записи данных в массив байтов
            try (ObjectOutputStream o = new ObjectOutputStream(b)) { //объект сериализации
                o.writeObject(command); // сериализуем
                o.flush();
            }
            byte[] serializedData = b.toByteArray(); //получаем массив байтов (сериализуемый объект)

            dataOutputStream.writeInt(serializedData.length);
            dataOutputStream.write(serializedData);
            dataOutputStream.flush();

            rootLogger.info("CommandContainer успешно отправлен клиенту: {}", command.toString());

        } catch (IOException ex) {
            rootLogger.error("Ошибка при отправке CommandContainer клиенту: {}", ex.getMessage());
        }
    }
}