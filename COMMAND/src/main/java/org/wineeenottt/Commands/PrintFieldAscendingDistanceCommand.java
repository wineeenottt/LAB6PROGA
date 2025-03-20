package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

/**
 * Класс PrintFieldAscendingDistanceCommand реализует интерфейс Command и представляет команду,
 * которая выводит значения поля distance всех элементов коллекции в порядке возрастания.
 */
public class PrintFieldAscendingDistanceCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для доступа к методам управления коллекцией и получения данных о расстояниях.
     */
    private CollectionManager collectionManager;

    public PrintFieldAscendingDistanceCommand(){
        super("print_field_ascending_distance");
    }
    /**
     * Конструктор класса PrintFieldAscendingDistanceCommand.
     *
     * @param collectionManager объект класса CollectionManager, используемый для управления коллекцией.
     */
    public PrintFieldAscendingDistanceCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду. Выводит значения поля distance всех элементов коллекции
     * в порядке возрастания. Если коллекция пуста, выводится соответствующее сообщение.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            }

        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            printStream.println(collectionManager.showRouteSortedDistance());
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда выводит значения поля distance
     * всех элементов коллекции в порядке возрастания.
     */
    @Override
    public String getDescription() {
        return "выводит значения поля distance всех элементов в порядке возрастания";
    }
}