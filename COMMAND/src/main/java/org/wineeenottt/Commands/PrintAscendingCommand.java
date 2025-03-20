package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

/**
 * Класс PrintAscendingCommand реализует интерфейс Command и представляет команду,
 * которая выводит элементы коллекции в порядке возрастания их ID.
 */
public class PrintAscendingCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для доступа к методам управления коллекцией и сортировки элементов.
     */
    private CollectionManager collectionManager;

    public PrintAscendingCommand(){
        super("print_ascending");
    }
    /**
     * Конструктор класса PrintAscendingCommand.
     *
     * @param collectionManager объект класса CollectionManager, используемый для управления коллекцией.
     */
    public PrintAscendingCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду. Выводит элементы коллекции в порядке возрастания их ID.
     * Если коллекция пуста, выводится соответствующее сообщение.
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
     *
     * @return строка с описанием команды, указывающая, что команда выводит элементы коллекции
     * в порядке возрастания их ID.
     */
    @Override
    public String getDescription() {
        return "выводит элементы коллекции в порядке возрастания";
    }
}
