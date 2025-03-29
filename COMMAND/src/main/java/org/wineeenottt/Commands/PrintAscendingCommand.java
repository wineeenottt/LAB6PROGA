package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;


public class PrintAscendingCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;

    public PrintAscendingCommand(){
        super("print_ascending");
    }
    /**
     * Конструктор класса PrintAscendingCommand.
     */
    public PrintAscendingCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
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
            printStream.println(collectionManager.showIdSortedCollection());
        }
    }

    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "выводит элементы коллекции в порядке возрастания";
    }
}
