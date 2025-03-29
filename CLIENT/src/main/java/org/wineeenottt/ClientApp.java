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

/**
 * Основной класс клиентского приложения, отвечающий за взаимодействие с сервером.
 *
 * Обеспечивает установку соединения с сервером, обработку пользовательских команд,
 * отправку запросов и получение ответов от сервера. Поддерживает механизм повторного
 * подключения при возникновении ошибок связи.
 */
public class ClientApp {
    /**
     * Логгер для записи.
     */
    private static final Logger rootLogger = LoggerFactory.getLogger(ClientApp.class);

    /**
     * Интерфейс для взаимодействия с пользователем.
     */
    private final UserIO userIO;

    /**
     * Исполнитель команд.
     */
    private final CommandInvoker commandInvoker;

    /**
     * Порт сервера для подключения.
     */
    private final int serverPort;

    /**
     * Максимальное количество попыток переподключения.
     */
    private static final int MAX_RECONNECT_ATTEMPTS = 5;

    /**
     * Конструктор клиентского приложения.
     *
     * @param serverPort порт сервера для подключения
     */
    public ClientApp(int serverPort) {
        this.serverPort = serverPort;
        this.userIO = new UserIO();
        this.commandInvoker = new CommandInvoker(userIO);
        rootLogger.info("Конструктор класса ClientApp был загружен.");
    }

    /**
     * Запускает основной цикл работы клиента
     *
     * Осуществляет попытки подключения к серверу, обрабатывает пользовательский ввод,
     * управляет отправкой команд и получением ответов. При возникновении ошибок
     * предлагает пользователю повторить попытку подключения.
     *
     */
    public void start() {
        int attempts = 0;
        while (attempts < MAX_RECONNECT_ATTEMPTS) { //пытаемся установить соединение
            ClientConnection clientConnection = null; //инициализация переменной clientConnection
            try {
                clientConnection = new ClientConnection();
                InetSocketAddress serverAddress = new InetSocketAddress("localhost", serverPort);
                clientConnection.connect(serverAddress);//устанавливаем соединение

                SocketChannel channel = clientConnection.getClientChannel();//возвращаем канал соединения или null
                if (channel == null || !channel.isConnected()) {
                    rootLogger.error("Не удалось установить соединение с сервером.");
                    attempts++;
                    if (attempts < MAX_RECONNECT_ATTEMPTS && retryConnection()) {
                        continue;
                    }
                    break;
                }

                rootLogger.info("Подключение установлено с сервером: localhost/127.0.0.1:{}", serverPort);
                ClientResponse clientResponse = new ClientResponse(channel); //объект отправитель
                ClientRequest clientRequest = new ClientRequest(channel); //объект получатель
                ClientCommandProcess commandProcess = new ClientCommandProcess(commandInvoker);//объект команд

                rootLogger.info("Клиент готов к чтению команд.");
                interactWithServer(clientResponse, clientRequest, commandProcess, serverAddress);
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

    /**
     * Основной цикл взаимодействия с сервером
     */
    private void interactWithServer(ClientResponse clientSend, ClientRequest clientRequest, ClientCommandProcess commandProcess, InetSocketAddress serverAddress) throws IOException {
        boolean isConnected = true; //флаг активности соединения
        boolean waiting = false; //флаг ожидания ответа от сервера
        CommandContainer lastSentCommand = null;

        while (isConnected) {
            if (!waiting) {
                System.out.println("Введите название команды:");
                userIO.printPreamble();
                String line = userIO.readLine().trim();
                if (line.equalsIgnoreCase("exit")) {
                    rootLogger.info("Клиент завершает работу по команде пользователя.");
                    break;
                }
                if (commandProcess.executeCommand(line)) {
                    if (commandProcess.executeCommand("help"))
                    {
                        continue;
                    }
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
                waiting = true;
            }

            try {
                CommandContainer request = clientRequest.readCommandContainer();
                if (request != null) {
                    rootLogger.info("Данные получены: {}", request);
                    System.out.println("Команда: " + request.getName());
                    System.out.println("Результат: " + request.getResult());
                    waiting = false;
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                rootLogger.error("Прерывание ожидания: {}", ex.getMessage());
                Thread.currentThread().interrupt();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Запрашивает у пользователя подтверждение на повторную попытку подключения
     */
    private boolean retryConnection() {
        rootLogger.warn("Повторить попытку? (y/n)");
        String result = userIO.readLine().trim().toLowerCase(Locale.ROOT);
        return result.equals("y");
    }
}