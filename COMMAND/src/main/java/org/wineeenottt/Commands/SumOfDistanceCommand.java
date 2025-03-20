package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

/**
 * Класс SumOfDistanceCommand реализует интерфейс Command и представляет команду,
 * которая вычисляет и выводит сумму значений поля distance для всех элементов коллекции.
 */
public class SumOfDistanceCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для доступа к методам управления коллекцией и вычисления суммы расстояний.
     */
    private CollectionManager collectionManager;

    public SumOfDistanceCommand(){
        super("sum_of_distance");
    }
    /**
     * Конструктор класса SumOfDistanceCommand.
     *
     * @param collectionManager объект класса CollectionManager, используемый для управления коллекцией.
     */
    public SumOfDistanceCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду. Вычисляет сумму значений поля distance для всех элементов коллекции
     * и выводит результат на экран.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream)
            throws CannotExecuteCommandException {
        if (invocationStatus == InvocationStatus.CLIENT) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            }
        } else if (invocationStatus == InvocationStatus.SERVER) {
            printStream.println("Сумма всех расстояний: " + collectionManager.sumOfDistance());
        }
    }


    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда выводит сумму значений поля distance
     * для всех элементов коллекции.
     */
    @Override
    public String getDescription() {
        return "выводит сумму значений поля distance для всех элементов коллекции";
    }
}