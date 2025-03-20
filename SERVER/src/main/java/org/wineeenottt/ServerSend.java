package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;

import java.io.*;
import java.net.Socket;

/**
 * Класс {@code ServerSend} отвечает за отправку данных клиенту через сокет.
 * Он сериализует объекты {@link CommandContainer} и отправляет их клиенту
 * с использованием {@link DataOutputStream}.
 */
public class ServerSend {
    private static final Logger rootLogger = LoggerFactory.getLogger(ServerSend.class);
    private final Socket clientSocket;
    private final DataOutputStream dataOutputStream;

    /**
     * Конструктор класса {@code ServerSend}.
     *
     * @param clientSocket сокет, подключенный к клиенту, через который будет осуществляться отправка данных.
     * @throws IOException если произошла ошибка при создании потока вывода.
     */
    public ServerSend(Socket clientSocket) throws IOException {
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
     *
     * @param command объект, который необходимо отправить клиенту.
     */
    public void sendCommandContainer(CommandContainer command) {
        try {
            if (clientSocket == null || clientSocket.isClosed()) {
                rootLogger.error("Нет активного соединения с клиентом");
                return;
            }

            ByteArrayOutputStream b = new ByteArrayOutputStream(); //буфер для записи данных в массив байтов
            try (ObjectOutputStream o = new ObjectOutputStream(b)) { //поток
                o.writeObject(command); // сериализуем
                o.flush(); //все данные записаны
            }
            byte[] serializedData = b.toByteArray(); //получаем массив байтов (сериализуемый объект)

            dataOutputStream.writeInt(serializedData.length);
            dataOutputStream.write(serializedData);
            dataOutputStream.flush(); //все отправили

            rootLogger.info("CommandContainer успешно отправлен клиенту: {}", command.toString());

        } catch (IOException ex) {
            rootLogger.error("Ошибка при отправке CommandContainer клиенту: {}", ex.getMessage());
        }
    }
}