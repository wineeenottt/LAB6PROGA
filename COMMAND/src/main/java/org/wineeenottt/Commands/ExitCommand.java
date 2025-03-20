package org.wineeenottt.Commands;

import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

/**
 * Класс ExitCommand реализует интерфейс Command и представляет команду завершения работы программы.
 * При выполнении команды программа завершает свою работу с выводом соответствующего сообщения.
 */
public class ExitCommand extends Command {

    /**
     * Конструктор класса ExitCommand.
     * Создает объект команды завершения работы программы.
     */
    public ExitCommand() {
        super("exit");}

    /**
     * Метод, выполняющий команду завершения работы программы.
     * После вывода сообщения о завершении программа завершает свою работу с кодом 0.
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
     *
     * @return строка с описанием команды, указывающая, что команда завершает работу программы.
     */
    @Override
    public String getDescription() {
        return "завершает работу программы";
    }
}