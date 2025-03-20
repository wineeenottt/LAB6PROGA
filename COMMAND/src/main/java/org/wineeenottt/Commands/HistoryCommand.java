package org.wineeenottt.Commands;

import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Класс HistoryCommand реализует интерфейс Command и представляет команду вывода истории выполненных команд.
 * Команда выводит список последних 11 выполненных команд.
 */
public class HistoryCommand extends Command {

    /**
     * Список, хранящий историю выполненных команд.
     */
    private ArrayList<String> commandsHistoryList;

    public HistoryCommand(){
        super("history");
    }
    /**
     * Конструктор класса HistoryCommand.
     *
     * @param commandsHistoryList список, содержащий историю выполненных команд.
     */
    public HistoryCommand(ArrayList<String> commandsHistoryList) {
        this.commandsHistoryList = commandsHistoryList;
    }

    /**
     * Метод, выполняющий команду. Выводит список последних выполненных команд.
     * Если список пуст, выводится соответствующее сообщение.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            }
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            StringBuilder sb=new StringBuilder();
            sb.append("History: \n");
            for(String str: commandsHistoryList)
                sb.append(str).append("\n");
            printStream.println(sb);
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда выводит последние 11 выполненных команд.
     */
    @Override
    public String getDescription() {
        return "вывести последние 11 команд";
    }
}
