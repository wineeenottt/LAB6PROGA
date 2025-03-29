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
     */
    public HelpCommand(HashMap<String, Command> commandMap) {
        super("help");
        this.commandMap = commandMap;
    }

    /**
     * Метод, выполняющий команду.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            } else {
                 commandMap.forEach((key, value) -> System.out.println(key + ": " + value.getDescription()));
            }
        }
    }

    /**
     * Метод, возвращающий описание данной команды.
     */
    @Override
    public String getDescription() {
        return "выводит справку по всем командам";
    }
}

