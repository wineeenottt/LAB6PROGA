package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class  MainServer {
    private static final Logger rootLogger =(Logger) LoggerFactory.getLogger(MainServer.class);
    public static void main(String[] args){
        ServerApp application = new ServerApp( );

        if (args.length > 0) {
            if (!args[0].isEmpty()) {
                System.out.println("Старт с аргументом");

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    rootLogger.info("Сохранение коллекции в файле.");
                    application.getCollectionManager().save(args[0]);
                    rootLogger.info("Коллекция была сохранена {}", args[0]);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        rootLogger.error("Ошибка с потоками: "+ ex);
                    }
                    rootLogger.info("Завершение работы сервера.");
                }));
                try {
                    application.start(args[0]);
                } catch ( IOException ex) {
                    rootLogger.warn("По указанному адресу нет подходящего файла {}", args[0]);
                }
            }
        }
        else {
            System.out.println("Старт без аргумента");
            String file = "FILES/RouteStorage";
            try {
                application.start(file);
            } catch (IOException ex ) {
                rootLogger.warn("По указанному адресу нет подходящего файла " + file);
            }
        }
    }
}
