package org.wineeenottt.Commands;

import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;
import java.util.ArrayList;

public class HistoryCommand extends Command {

    /**
     * Список, хранящий историю выполненных команд.
     */
    private ArrayList<String> commandsHistoryList;

    public HistoryCommand() {
        super("history");
    }

    /**
     * Конструктор класса HistoryCommand.
     */
    public HistoryCommand(ArrayList<String> commandsHistoryList) {
        this();
        this.commandsHistoryList = commandsHistoryList;
    }

    /**
     * Метод, выполняющий команду.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            }
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            StringBuilder sb = new StringBuilder();
            sb.append("History: \n");
            for (String str : commandsHistoryList)
                if (commandsHistoryList.indexOf(str) != commandsHistoryList.size() - 1) {
                    sb.append(str).append("\n");
                } else {
                    sb.append(str);
                }

            printStream.print(sb);
        }
    }

    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "вывести последние 11 команд";
    }
}