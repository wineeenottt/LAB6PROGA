package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;
import org.wineeenottt.IO.UserIO;
import org.wineeenottt.Utility.RouteFieldsReader;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

/**
 * Класс, через который осуществляется исполнение команд. Хранит коллекции всех существующих команд.
 */
public class CommandInvoker {
    /**
     * Коллекция команд для клиента
     */
    private HashMap<String, Command> clientCommands;
    /**
     * Коллекция команд для сервера
     */
    private HashMap<String, Command> serverCommands;

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;
    /**
     * Поле, хранящее ссылку на объект класса UserIO.
     */
    private UserIO userIO;
    /**
     * Поле, хранящее путь к файлу для сохранения коллекции.
     */
    private String inputFile;
    /**
     * Поле, хранящее входные данные для команды add.
     */
    private String inputData;
    /**
     * Контейнер с последней выполненной командой.
     */
    private CommandContainer lastCommandContainer;
    /**
     * Поле, хранящее ссылку на объект, осуществляющий чтение полей из указанного в userIO потока ввода.
     */
    private RouteFieldsReader routeFieldsReader;
    /**
     * Поле, хранящее объект класса ExecuteScript.Script.
     */
    private ExecuteScriptCommand.Script script;
    /**
     * Поле, хранящее список истории команд
     */
    private ArrayList<String> commandsHistoryList = new ArrayList<>();

    /**
     * Конструктор для клиента.
     *
     * @param userIO читает данные из указанного потока.
     */
    public CommandInvoker(UserIO userIO) {
        this.clientCommands = new HashMap<>();
        this.userIO = userIO;
        this.routeFieldsReader = new RouteFieldsReader(userIO);
        this.script = new ExecuteScriptCommand.Script();
        this.putClientCommands();
       // System.out.println("Элементы коллекции для клиента были загружены.");
    }

    /**
     * Конструктор для клиента при выполнении скрипта.
     *
     * @param userIO            читает данные из указанного потока.
     * @param routeFieldsReader объект для чтения полей маршрута.
     * @param script            скрипт, хранит пути до файлов.
     */
    public CommandInvoker(UserIO userIO, RouteFieldsReader routeFieldsReader, ExecuteScriptCommand.Script script) {
        this.clientCommands = new HashMap<>();
        this.userIO = userIO;
        this.routeFieldsReader = routeFieldsReader;
        this.script = script;
        this.putClientCommands();
    }

    /**
     * Конструктор для сервера с файлом сохранения.
     *
     * @param collectionManager менеджер коллекции.
     * @param inputFile         файл для сохранения коллекции.
     */
    public CommandInvoker(CollectionManager collectionManager, String inputFile) {
        this.serverCommands = new HashMap<>();
        this.collectionManager = collectionManager;
        this.inputFile = inputFile;
        this.script = new ExecuteScriptCommand.Script();
        this.putServerCommands();
       // System.out.println("Элементы коллекции для сервера были загружены.");
    }

    /**
     * Конструктор для сервера без файла.
     *
     * @param collectionManager менеджер коллекции.
     */
    public CommandInvoker(CollectionManager collectionManager) {
        this.serverCommands = new HashMap<>();
        this.collectionManager = collectionManager;
        this.putServerCommands();
    }

    /**
     * Метод, добавляющий клиентские команды в коллекцию.
     */
    private void putClientCommands() {
        clientCommands.put("info", new InfoCommand());
        clientCommands.put("show", new ShowCommand());
        clientCommands.put("clear", new ClearCommand());
        clientCommands.put("exit", new ExitCommand());
        clientCommands.put("help", new HelpCommand(clientCommands));
        clientCommands.put("add", new AddCommand(routeFieldsReader));
        clientCommands.put("update", new UpdateElementCommand(userIO));
        clientCommands.put("remove_by_id", new RemoveByIdCommand());
        clientCommands.put("execute_script", new ExecuteScriptCommand(userIO, routeFieldsReader, script, inputFile, inputData));
        clientCommands.put("sum_of_distance", new SumOfDistanceCommand());
        clientCommands.put("remove_greater", new RemoveGreaterCommand());
        clientCommands.put("print_field_ascending_distance", new PrintFieldAscendingDistanceCommand());
        clientCommands.put("print_ascending", new PrintAscendingCommand());
        clientCommands.put("add_if_max", new AddIfMaxCommand(routeFieldsReader));
        clientCommands.put("history", new HistoryCommand(commandsHistoryList));
    }

    /**
     * Метод, добавляющий серверные команды в коллекцию.
     */
    private void putServerCommands() {
        serverCommands.put("info", new InfoCommand(collectionManager));
        serverCommands.put("show", new ShowCommand(collectionManager));
        serverCommands.put("clear", new ClearCommand(collectionManager));
        serverCommands.put("save", new SaveCommand(collectionManager, inputFile));
        serverCommands.put("help", new HelpCommand(serverCommands));
        serverCommands.put("add", new AddCommand(collectionManager));
        serverCommands.put("update", new UpdateElementCommand(collectionManager));
        serverCommands.put("remove_by_id", new RemoveByIdCommand(collectionManager));
        serverCommands.put("execute_script", new ExecuteScriptCommand(collectionManager));
        serverCommands.put("sum_of_distance", new SumOfDistanceCommand(collectionManager));
        serverCommands.put("remove_greater", new RemoveGreaterCommand(collectionManager));
        serverCommands.put("print_field_ascending_distance", new PrintFieldAscendingDistanceCommand(collectionManager));
        serverCommands.put("print_ascending", new PrintAscendingCommand(collectionManager));
        serverCommands.put("add_if_max", new AddIfMaxCommand(collectionManager));
        serverCommands.put("history", new HistoryCommand(commandsHistoryList));
    }

    /**
     * Метод, выполняющий команду на клиенте.
     */
    public boolean executeClient(String firstCommandLine, PrintStream printStream) {
        String[] words = firstCommandLine.trim().split("\\s+");
        String commandName = words[0].toLowerCase(Locale.ROOT);
        String[] arguments = Arrays.copyOfRange(words, 1, words.length);

        try {
            if (clientCommands.containsKey(commandName)) {
                Command command = clientCommands.get(commandName);
                command.execute(arguments, InvocationStatus.CLIENT, printStream);
                lastCommandContainer = new CommandContainer(command.getName(), command.getResult());
                addToCommandsHistory(commandName);
                return true;
            } else {
                printStream.println("Команда " + commandName + " не распознана.");
                return false;
            }
        } catch (NullPointerException ex) {
            printStream.println("Ошибка: команда " + commandName + " вызвала NullPointerException.");
            return false;
        } catch (CannotExecuteCommandException ex) {
            printStream.println("Ошибка выполнения команды " + commandName + ": " + ex.getMessage());
            return false;
        }
    }

    /**
     * Метод, выполняющий команду на сервере.
     */
    public boolean executeServer(String firstCommandLine, ArrayList<Object> result, PrintStream printStream) {
        String[] words = firstCommandLine.trim().split("\\s+");
        String commandName = words[0].toLowerCase(Locale.ROOT);
        String[] arguments = Arrays.copyOfRange(words, 1, words.length);

        try {
            if (serverCommands.containsKey(commandName)) {
                Command command = serverCommands.get(commandName);
                command.setResult(result);
                command.execute(arguments, InvocationStatus.SERVER, printStream);
                addToCommandsHistory(commandName);
                return true;
            } else {
                printStream.println("Команда " + commandName + " не поддерживается на сервере.");
                return false;
            }
        } catch (NullPointerException ex) {
            printStream.println("Ошибка на сервере: команда " + commandName + " вызвала NullPointerException.");
            ex.printStackTrace();
            return false;
        } catch (CannotExecuteCommandException ex) {
            printStream.println("Ошибка выполнения на сервере команды " + commandName + ": " + ex.getMessage());
            return false;
        }
    }

    /**
     * Метод, возвращающий контейнер последней выполненной команды.
     *
     * @return CommandContainer контейнер с командой.
     */
    public CommandContainer getLastCommandContainer() {
        return lastCommandContainer;
    }

    /**
     * Метод, добавляющий команду в историю.
     *
     * @param string имя команды для добавления.
     */
    private void addToCommandsHistory(String string) {
        if (commandsHistoryList.size() == 11) {
            commandsHistoryList.remove(0);
        }
        commandsHistoryList.add(string);
    }
}
