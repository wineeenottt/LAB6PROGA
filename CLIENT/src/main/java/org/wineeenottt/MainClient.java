package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClient {
    //логгер
    private static final Logger rootLogger = (Logger) LoggerFactory.getLogger(MainClient.class);
    //порт по умолчанию
    private static final int DEFAULT_PORT = 7777;

    public static void main(String[] args) {
        try {
            //арг ком стр не пусто,то этот порт,иначе по умолчанию
            int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
            rootLogger.info("Запуск клиента с портом: {}", port);
            ClientApp application = new ClientApp(port);
            application.start();
        } catch (NumberFormatException ex) {
            rootLogger.error("Значение порта должно быть целочисленным. Введено: {}",
                    args.length > 0 ? args[0] : "null");
            System.exit(1);
        }
    }
}