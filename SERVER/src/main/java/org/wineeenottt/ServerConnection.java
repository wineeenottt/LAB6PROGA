package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;

/**
 * Класс ServerConnection отвечает за создание и управление серверным сокетом.
 */
public class ServerConnection {
    private static final Logger rootLogger = (Logger) LoggerFactory.getLogger(ServerConnection.class);
    private final ServerSocketChannel serverSocketChannel;

    /**
     * Конструктор класса ServerConnection.
     */
    public ServerConnection(int port) throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(port));
        rootLogger.info("Сервер запущен на порту: {}", port);
    }

    /**
     * Возвращает объект ServerSocket, связанный с каналом серверного сокета.
     */
    public ServerSocket getServerSocketChannel() {
        return serverSocketChannel.socket();
    }
}