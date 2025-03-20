package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

/**
 * Класс ClearCommand реализует интерфейс Command и представляет команду очистки коллекции.
 * Команда удаляет все элементы из коллекции и выводит соответствующее сообщение.
 */
public class ClearCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для доступа к методам управления коллекцией.
     */
    private CollectionManager collectionManager;

    public ClearCommand() {
        super("clear");}
    /**
     * Конструктор класса ClearCommand.
     *
     * @param collectionManager объект класса CollectionManager, используемый для управления коллекцией.
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
            printStream.println("Коллекция " + collectionManager.getClass().getSimpleName() + "очищена");
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда очищает все элементы коллекции.
     */
    @Override
    public String getDescription() {
        return "очищает все элементы коллекции";
    }

}