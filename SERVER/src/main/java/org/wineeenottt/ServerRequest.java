package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;

import java.io.*;
import java.net.Socket;

/**
 * Класс ServerReader отвечает за чтение данных от клиента, подключенного к серверу.
 *
 */
public class ServerRequest {
    /**
     * Логгер для записи.
     */
    private static final Logger rootLogger = (Logger) LoggerFactory.getLogger(ServerRequest.class);
    /**
     * Канал для связи с клиентом.
     */
    private final Socket clientSocket;
    /**
     * Поток ввода для чтения данных от клиента.
     */
    private final DataInputStream dataInputStream;

    /**
     * Конструктор класса ServerReader.
     */
    public ServerRequest(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        try {
            this.dataInputStream = new DataInputStream(clientSocket.getInputStream());
            rootLogger.info("Поток ввода для клиента инициализирован");
        } catch (IOException ex) {
            rootLogger.error("Ошибка при создании потока ввода: {}", ex.getMessage());
            throw ex;
        }
    }

    /**
     * Читает данные от клиента.
     */
    public CommandContainer readCommandContainer() throws IOException, ClassNotFoundException {
        try {
            //проверяем активность соединения
            if (clientSocket == null || clientSocket.isClosed()) {
                rootLogger.error("Нет соединения с клиентом");
                throw new IOException("Нет соединения с клиентом");
            }

            int dataSize = dataInputStream.readInt(); //читаем размер данных
            if (dataSize <= 0) {
                rootLogger.error("Некорректный размер данных: {}", dataSize);
                throw new IOException("Некорректный размер данных");
            }

            byte[] data = new byte[dataSize];//создаем массив
            dataInputStream.readFully(data); //читаем данные из потока

            try (ByteArrayInputStream b = new ByteArrayInputStream(data);
                 ObjectInputStream o = new ObjectInputStream(b)) {
                Object obj = o.readObject(); //дес
                if (!(obj instanceof CommandContainer result)) {
                    rootLogger.error("Получен объект неверного типа: {}", obj.getClass().getName());
                    throw new ClassNotFoundException("Ожидался CommandContainer, получен " + obj.getClass().getName());
                }
                rootLogger.info("Получен CommandContainer от клиента: {}", result.toString());
                return result;
            }

        } catch (IOException ex) {
            rootLogger.error("Ошибка при чтении данных от клиента: {}", ex.getMessage());
            throw ex;
        } catch (ClassNotFoundException ex) {
            rootLogger.error("Ошибка: {}", ex.getMessage());
            throw ex;
        }
    }
}