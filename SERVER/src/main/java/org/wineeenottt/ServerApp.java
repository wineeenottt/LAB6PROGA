package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Collection.Route;
import org.wineeenottt.Commands.CommandContainer;
import org.wineeenottt.Commands.CommandInvoker;
import org.wineeenottt.IO.UserIO;
import org.wineeenottt.WorkWithFile.FileManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.*;


public class ServerApp {
    private CollectionManager collectionManager;
    private FileManager fileManager;
    private UserIO userIO;
    private CommandInvoker commandInvoker;
    private ServerConnection serverConnection;
    private ServerSocket serverSocket;
    private boolean isConnected;

    private static final Logger rootLogger = (Logger) LoggerFactory.getLogger(ServerApp.class);

    public ServerApp() {
        fileManager = new FileManager();
        Set<Route> initialRoutes;
        try {
            initialRoutes = fileManager.parseCsvFile("FILES/RouteStorage");
            if (initialRoutes == null) {
                initialRoutes = new HashSet<>();
            }
        } catch (IOException e) {
            initialRoutes = new HashSet<>();
            rootLogger.error("Ошибка при загрузке файла: {}. Используется пустая коллекция", e.getMessage());
        }
        collectionManager = new CollectionManager(initialRoutes);
        userIO = new UserIO();
    }
    public void start(String inputFile) throws IOException {
        try {

            File ioFile = new File(inputFile);
            if (!ioFile.canWrite() || ioFile.isDirectory() || !ioFile.isFile()) {
                throw new IOException("Недоступен файл для записи или это не файл: " + inputFile);
            }

//            Set<Route> routes = fileManager.parseCsvFile(inputFile);
//            for (Route route : routes) {
//                collectionManager.addWithName(route.getId(), route, System.out);
//            }

            this.commandInvoker = new CommandInvoker(collectionManager, inputFile);
            rootLogger.info("Элементы коллекции из файла {} были загружены.", inputFile);

            // Настройка подключения
//            Scanner scanner = new Scanner(System.in);
//            int port;
//            do {
//                System.out.print("Введите порт: ");
//                port = scanner.nextInt();
//                if (port <= 0) {
//                    rootLogger.error("Введенный порт невалиден.");
//                } else {
//                    serverConnection = new ServerConnection(port);
//                    serverSocket = serverConnection.getServerSocketChannel();
//                    isConnected = true;
//                }
//            } while (!isConnected);
            int port = 7777;
            serverConnection = new ServerConnection(port);
                    serverSocket = serverConnection.getServerSocketChannel();
                    isConnected = true;

            rootLogger.info("Порт установлен: {}", port);

            cycle();

        } catch (NoSuchElementException ex) {
            rootLogger.error("Аварийное завершение работы: {}", ex.getMessage());
            System.exit(-1);
        } catch (Exception ex) {
            rootLogger.error("Ошибка при запуске сервера: {}", ex.getMessage());
            throw new IOException(ex);
        }
    }

    private void cycle() {
        while (isConnected) {
            try {
                Socket clientSocket = serverSocket.accept();
                rootLogger.info("Новое подключение от клиента: {}", clientSocket.getRemoteSocketAddress());

                handleClient(clientSocket.getChannel());

            } catch (IOException ex) {
                rootLogger.warn("Ошибка при принятии подключения: {}", ex.getMessage());
                isConnected = false;
            }
        }
    }

    private void handleClient(SocketChannel clientChannel) {
        try {
            ServerReader requestReader = new ServerReader(clientChannel.socket());
            ServerSend responseSender = new ServerSend(clientChannel.socket());
            ServerCommandProcess commandProcessor = new ServerCommandProcess(commandInvoker);

            while (clientChannel.isOpen()) {
                try {
                    CommandContainer command = requestReader.readCommandContainer();
                    rootLogger.info("Обрабатывается команда: {}", command.getName());

                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(b);
                    commandProcessor.executeCommand(command, printStream);

                    ArrayList<Object> responseData = new ArrayList<>();
                    responseData.add(b.toString());
                    CommandContainer response = new CommandContainer(command.getName(), responseData);
                    responseSender.sendCommandContainer(response);
                } catch (IOException ex) {
                    rootLogger.warn("Ошибка: {}", ex.getMessage());
                    break;
                } catch (ClassNotFoundException ex) {
                    rootLogger.error("Ошибка: {}", ex.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}
