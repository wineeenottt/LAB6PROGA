package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Главный класс клиентского приложения.
 */
public class MainClient {
    /**
     * Логгер для записи.
     */
    private static final Logger rootLogger = LoggerFactory.getLogger(MainClient.class);

    /**
     * Порт, используемый клиентом для подключения к серверу.
     */
    private static final int PORT = 7778;

    /**
     * Точка входа клиентского приложения.
     */
    public static void main(String[] args) {
        try {
            rootLogger.info("Запуск клиента с портом: {}", PORT);
            ClientApp application = new ClientApp(PORT);
            application.start();
        } catch (Exception ex) {
            rootLogger.error("Ошибка при запуске клиента: {}", ex.getMessage());
            System.exit(1);
        }
    }
}