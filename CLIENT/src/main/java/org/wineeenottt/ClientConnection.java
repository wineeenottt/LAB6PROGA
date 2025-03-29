package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Класс ClientConnection отвечает за подключение клиента к серверу через SocketChannel.
 * Используется для установки неблокирующего соединения с сервером.
 */
public class ClientConnection {
    /**
     * Логгер для записи.
     */
    private static final Logger rootLogger = LoggerFactory.getLogger(ClientConnection.class);
    private SocketChannel clientChannel;

    /**
     * Устанавливает соединение с сервером по указанному адресу.
     */
    public void connect(InetSocketAddress inetServerAddress) throws IOException {
        try {
            clientChannel = SocketChannel.open();//открываем сокет
            clientChannel.configureBlocking(false);//неблкирующий режим
            clientChannel.connect(inetServerAddress);//подключаемся
            while (!clientChannel.finishConnect()) {
                Thread.sleep(100); // ожидание подключения
            }
            rootLogger.info("Подключение установлено с сервером: {}", inetServerAddress);
        } catch (IOException ex) {
            rootLogger.error("Ошибка при установке соединения: {}", ex.getMessage());
            throw ex;
        } catch (InterruptedException ex) {
            rootLogger.error("Прерывание при подключении: {}", ex.getMessage());
            Thread.currentThread().interrupt();
            throw new IOException("Прерывание подключения", ex);
        }
    }

    /**
     * Возвращает канал SocketChannel, используемый для соединения с сервером.
     */
    public SocketChannel getClientChannel() {
        return clientChannel;
    }
}