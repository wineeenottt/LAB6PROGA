package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;
import org.wineeenottt.Exceptions.RecoursiveCallException;
import org.wineeenottt.IO.UserIO;
import org.wineeenottt.Utility.RouteFieldsReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Класс, управляющий выполнением скрипта
 */
public class ExecuteScriptCommand extends Command {

    private CollectionManager collectionManager;
    private UserIO userIO;
    private RouteFieldsReader routeFieldsReader;
    private String scriptPath;
    private Script script;
    private String inputFile;
    private String inputData;

    public ExecuteScriptCommand(UserIO userIO, RouteFieldsReader routeFieldsReader, Script script, String inputFile, String inputData) {
        super("execute_script");
        this.userIO = userIO;
        this.routeFieldsReader = routeFieldsReader;
        this.script = script;
        this.inputFile = inputFile;
        this.inputData = inputData;
    }

    public ExecuteScriptCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.CLIENT)) {
            result = new ArrayList<>();
            try {
                CommandInvoker commandInvoker;
                Scanner scanner;
                if (arguments.length == 2) {
                    scriptPath = arguments[0].trim();
                    if (script.scriptPaths.contains(scriptPath)) {
                        throw new RecoursiveCallException(scriptPath);
                    }
                    script.putScript(scriptPath);

                    File scriptFile = new File(scriptPath);
                    if (!scriptFile.exists() || !scriptFile.isFile() || !scriptFile.canRead()) {
                        throw new IOException("Файл скрипта недоступен для чтения");
                    }

                    String dataPath = arguments[1].trim();
                    File dataFile = new File(dataPath);
                    if (!dataFile.exists() || !dataFile.isFile() || !dataFile.canRead()) {
                        throw new IOException("Файл данных недоступен для чтения");
                    }

                    FileInputStream fileInputStream = new FileInputStream(scriptFile);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    scanner = new Scanner(inputStreamReader);
                    userIO = new UserIO(scanner);
                    routeFieldsReader.setInputData(dataPath);
                    commandInvoker = new CommandInvoker(userIO, routeFieldsReader, script);

                    super.result.add(scriptPath);
                    PrintStream nullStream = new PrintStream(new OutputStream() {
                        public void write(int b) {
                        }
                    });

                    while (scanner.hasNext()) {
                        String commandLine = scanner.nextLine().trim();
                        if (!commandLine.isEmpty()) {
                            if (commandInvoker.executeClient(commandLine, nullStream)) {
                                super.result.add(commandInvoker.getLastCommandContainer());
                            }
                        }
                    }
                    scanner.close();
                    script.removeScript(scriptPath);
                }
            } catch (FileNotFoundException ex) {
                printStream.println("Файл скрипта не найден");
                throw new CannotExecuteCommandException("Ошибка выполнения скрипта: " + ex.getMessage());
            } catch (IOException ex) {
                printStream.println("Доступ к файлу невозможен: " + ex.getMessage());
                throw new CannotExecuteCommandException("Ошибка выполнения скрипта: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                printStream.println("Неверное количество аргументов: " + ex.getMessage());
                throw new CannotExecuteCommandException("Ошибка выполнения скрипта: " + ex.getMessage());
            } catch (RecoursiveCallException ex) {
                printStream.println("Рекурсивный вызов скрипта: " + scriptPath);
                throw new CannotExecuteCommandException("Ошибка выполнения скрипта: " + ex.getMessage());
            } finally {
                if (arguments.length == 2) {
                    script.removeScript(scriptPath);
                }
            }
        } else if (invocationEnum.equals(InvocationStatus.SERVER)) {
            printStream.println("Команды, исполняемые скриптом: " + this.getResult().get(0));
            Object[] arr = result.toArray();
            arr = Arrays.copyOfRange(arr, 1, arr.length);
            CommandContainer[] containerArray = Arrays.copyOf(arr, arr.length, CommandContainer[].class);

            CommandInvoker commandInvoker = new CommandInvoker(collectionManager);
            for (CommandContainer command : containerArray) {
                commandInvoker.executeServer(command.getName(), command.getResult(), printStream);
            }
        }
    }

    @Override
    public String getDescription() {
        return "выполняет команды, описанные в скрипте или введенные с консоли";
    }

    static class Script {
        private final ArrayList<String> scriptPaths = new ArrayList<>();

        public void putScript(String scriptPath) {
            scriptPaths.add(scriptPath);
        }

        public void removeScript(String scriptPath) {
            scriptPaths.remove(scriptPath);
        }
    }
}