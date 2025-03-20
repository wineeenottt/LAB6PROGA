package org.wineeenottt.Commands;

import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends Command {

    /**
     * Коллекция, содержащая объекты всех доступных в программе команд.
     * Ключом является имя команды, а значением — объект команды.
     */
    private final Map<String, Command> commandMap;

    /**
     * Конструктор класса HelpCommand.
     *
     * @param commandMap коллекция, содержащая все доступные команды.
     */
    public HelpCommand(HashMap<String, Command> commandMap) {
        super("help");
        this.commandMap = commandMap;
    }

    /**
     * Метод, выполняющий команду. Выводит описание всех доступных в программе команд.
     * Для каждой команды выводится её имя и описание.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            } else {
                 commandMap.forEach((key, value) -> System.out.println(key + ": " + value.getDescription()));
            }
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            commandMap.forEach((key, value) -> System.out.println(key + ": " + value.getDescription()));
        }
    }

    /**
     * Метод, возвращающий описание данной команды.
     *
     * @return строка с описанием команды, указывающая, что команда выводит справку по всем командам.
     */
    @Override
    public String getDescription() {
        return "выводит справку по всем командам";
    }
}

