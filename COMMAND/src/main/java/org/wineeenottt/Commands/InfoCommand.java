package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class InfoCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для получения информации о коллекции.
     */
    private CollectionManager collectionManager;

    public InfoCommand() {
        super("info");
    }

    /**
     * Конструктор класса InfoCommand.
     *
     * @param collectionManager объект класса CollectionManager, используемый для получения информации о коллекции.
     */
    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду. Выводит информацию о коллекции, включая тип коллекции,
     * дату инициализации, количество элементов и тип элементов коллекции.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            }
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            printStream.println(collectionManager.infoAboutCollection());
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда выводит информацию о коллекции.
     */
    @Override
    public String getDescription() {
        return "получить информацию о коллекции (тип, дата инициализации, кол-во элементов, тип элементов коллекции)";
    }
}


