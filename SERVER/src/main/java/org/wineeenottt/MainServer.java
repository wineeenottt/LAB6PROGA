package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

public class  MainServer {
    /**
     * Логгер для записи.
     */
    private static final Logger rootLogger = LoggerFactory.getLogger(MainServer.class);
    /**
     * Путь к файлу с коллекцией.
     */
    private static final String FILE = "FILES/RouteStorage";

    /**
     * Точка входа клиентского приложения.
     */
    public static void main(String[] args){
        ServerApp application = new ServerApp( );

            System.out.println("Старт работы сервера");
            try {
                application.start(FILE);
            } catch (IOException ex ) {
                rootLogger.warn("По указанному адресу нет подходящего файла " + FILE);
            }
        }
    }

