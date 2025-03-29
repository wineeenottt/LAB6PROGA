package org.wineeenottt.Commands;

import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class ExitCommand extends Command {

    /**
     * Конструктор класса ExitCommand.
     */
    public ExitCommand() {
        super("exit");}

    /**
     * Метод, выполняющий команду завершения работы программы.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            } else {
                printStream.println("Работа клиента завершена");
                System.exit(0);
            }
        }
    }

    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "завершает работу программы";
    }
}