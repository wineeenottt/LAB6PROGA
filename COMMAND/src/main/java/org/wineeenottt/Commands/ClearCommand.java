package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class ClearCommand extends Command {
    private static final String FILE = "FILES/RouteStorage";

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;

    public ClearCommand() {
        super("clear");}
    /**
     * Конструктор класса ClearCommand.
     */
    public ClearCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }


    /**
     * Метод, выполняющий команду. Очищает коллекцию и выводит сообщение об успешной очистке.
     */
    @Override
    public void execute(String[]arguments, InvocationStatus invocationStatus, PrintStream printStream) throws
            CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            }
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            collectionManager.clearAllCollection();
            collectionManager.save(FILE);
            printStream.println("Коллекция " + collectionManager.getClass().getSimpleName() + " очищена");
        }
    }

    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "очищает все элементы коллекции";
    }

}