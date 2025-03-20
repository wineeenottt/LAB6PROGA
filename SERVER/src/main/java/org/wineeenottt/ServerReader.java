package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;

import java.io.*;
import java.net.Socket;

/**
 * Класс ServerReader отвечает за чтение данных от клиента, подключенного к серверу.
 * Он использует сокет для получения данных и преобразует их в объект типа CommandContainer.
 *
 */
public class ServerReader {
    private static final Logger rootLogger = (Logger) LoggerFactory.getLogger(ServerReader.class);
    private final Socket clientSocket;
    private final DataInputStream dataInputStream;

    /**
     * Конструктор класса ServerReader.
     * Инициализирует поток ввода данных от клиента.
     *
     * @param clientSocket сокет, подключенный к клиенту
     * @throws IOException если возникает ошибка при создании потока ввода
     */
    public ServerReader(Socket clientSocket) throws IOException {
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
     * Читает данные от клиента и преобразует их в объект типа CommandContainer.
     *
     * Метод сначала проверяет, что соединение с клиентом активно, затем читает размер данных,
     * проверяет их корректность и десериализует данные в объект CommandContainer.
     *
     * @return объект CommandContainer, полученный от клиента
     * @throws IOException            если возникает ошибка ввода-вывода при чтении данных
     * @throws ClassNotFoundException если полученный объект не является типом CommandContainer
     */
    public CommandContainer readCommandContainer() throws IOException, ClassNotFoundException {
        try {
            if (clientSocket == null || clientSocket.isClosed()) {
                rootLogger.error("Нет соединения с клиентом");
                throw new IOException("Нет соединения с клиентом");
            }

            int dataSize = dataInputStream.readInt();
            if (dataSize <= 0) {
                rootLogger.error("Некорректный размер данных: {}", dataSize);
                throw new IOException("Некорректный размер данных");
            }

            byte[] data = new byte[dataSize];
            dataInputStream.readFully(data);

            try (ByteArrayInputStream b = new ByteArrayInputStream(data);
                 ObjectInputStream o = new ObjectInputStream(b)) {
                Object obj = o.readObject();
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