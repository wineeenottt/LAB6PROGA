package org.wineeenottt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;
import org.wineeenottt.Commands.CommandInvoker;
import org.wineeenottt.IO.UserIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Locale;

public class ClientApp {
    private static final Logger rootLogger = LoggerFactory.getLogger(ClientApp.class);
    private final UserIO userIO;
    private final CommandInvoker commandInvoker;
    private final int serverPort;
    private static final int MAX_RECONNECT_ATTEMPTS = 5;

    public ClientApp(int serverPort) {
        this.serverPort = serverPort;
        this.userIO = new UserIO();
        this.commandInvoker = new CommandInvoker(userIO);
        rootLogger.info("Конструктор класса ClientApp был загружен.");
    }

    public void start() {
        int attempts = 0;
        while (attempts < MAX_RECONNECT_ATTEMPTS) {
            ClientConnection clientConnection = null;
            try {
                clientConnection = new ClientConnection();
                InetSocketAddress serverAddress = new InetSocketAddress("localhost", serverPort);
                clientConnection.connect(serverAddress);

                SocketChannel channel = clientConnection.getClientChannel();
                if (channel == null || !channel.isConnected()) {
                    rootLogger.error("Не удалось установить соединение с сервером.");
                    attempts++;
                    if (attempts < MAX_RECONNECT_ATTEMPTS && retryConnection()) {
                        continue;
                    }
                    break;
                }

                rootLogger.info("Подключение установлено с сервером: localhost/127.0.0.1:{}", serverPort);
                ClientSend clientSend = new ClientSend(channel);
                ClientReader clientReader = new ClientReader(channel);
                ClientCommandProcess commandProcess = new ClientCommandProcess(commandInvoker);

                rootLogger.info("Клиент готов к чтению команд.");
                interactWithServer(clientSend, clientReader, commandProcess, serverAddress);
                return;

            } catch (IOException ex) {
                attempts++;
                rootLogger.error("Ошибка подключения (попытка {}/{}): {}",
                        attempts, MAX_RECONNECT_ATTEMPTS, ex.getMessage());
                if (attempts < MAX_RECONNECT_ATTEMPTS && retryConnection()) {
                    continue;
                }
                rootLogger.info("Завершение работы клиента.");
                return;
            } finally {
                if (clientConnection != null && clientConnection.getClientChannel() != null) {
                    try {
                        clientConnection.getClientChannel().close();
                    } catch (IOException e) {
                        rootLogger.error("Ошибка при закрытии соединения: {}", e.getMessage());
                    }
                }
            }
        }
        rootLogger.error("Превышено количество попыток подключения.");
    }

    private void interactWithServer(ClientSend clientSend, ClientReader clientReader,
                                    ClientCommandProcess commandProcess, InetSocketAddress serverAddress)
            throws IOException {
        boolean isConnected = true;
        boolean waitingForResponse = false;
        CommandContainer lastSentCommand = null;

        while (isConnected) {
            if (!waitingForResponse) {
                System.out.println("Введите название команды:");
                userIO.printPreamble();
                String line = userIO.readLine().trim();
                if (line.equalsIgnoreCase("exit")) {
                    rootLogger.info("Клиент завершает работу по команде пользователя.");
                    break;
                }

                if (!commandProcess.executeCommand(line)) {
                    rootLogger.warn("Команда не была выполнена на клиенте: {}", line);
                    continue;
                }

                lastSentCommand = commandInvoker.getLastCommandContainer();

                if (!clientSend.sendContainer(lastSentCommand)) {
                    rootLogger.error("Не удалось отправить CommandContainer: {}", lastSentCommand);
                    if (!retryConnection()) {
                        isConnected = false;
                    }
                    continue;
                }
                rootLogger.info("Данные отправлены: {}", lastSentCommand);
                waitingForResponse = true;
            }

            try {
                CommandContainer response = clientReader.readCommandContainer();
                if (response != null) {
                    rootLogger.info("Данные получены: {}", response);
                    System.out.println("Команда: " + response.getName());
                    System.out.println("Результат: " + response.getResult() + "\n");
                    waitingForResponse = false;
                } else {
                    Thread.sleep(100);
                }
            } catch (IOException | ClassNotFoundException ex) {
                rootLogger.error("Ошибка {}", ex.getMessage());
            } catch (InterruptedException ex) {
                rootLogger.error("Прерывание ожидания: {}", ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
    private boolean retryConnection() {
        rootLogger.warn("Повторить попытку? (y/n)");
        String result = userIO.readLine().trim().toLowerCase(Locale.ROOT);
        return result.equals("y");
    }
}