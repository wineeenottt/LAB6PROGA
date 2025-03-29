package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class ShowCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для доступа к методам управления коллекцией и отображения её элементов.
     */
    private CollectionManager collectionManager;

    public ShowCommand(){
        super("show");
    }
    /**
     * Конструктор класса ShowCommand.
     */
    public ShowCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду..
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            }
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            printStream.println(collectionManager.showElementsCollection());
        }
    }
    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "показывает подробное содержимое всех элементов коллекции";
    }
}