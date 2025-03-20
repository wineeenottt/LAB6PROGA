package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;
import org.wineeenottt.Utility.RouteFieldValidation;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Класс RemoveGreaterCommand реализует интерфейс CommandWithArguments и представляет команду,
 * которая удаляет из коллекции все элементы, ID которых превышает заданный.
 */
public class RemoveGreaterCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для управления коллекцией и удаления элементов.
     */
    private CollectionManager collectionManager;


    public RemoveGreaterCommand(){
        super("remove_greater");
    }
    /**
     * Конструктор класса RemoveGreaterCommand.
     *
     * @param collectionManager объект класса CollectionManager, используемый для управления коллекцией.
     */
    public RemoveGreaterCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду. Удаляет из коллекции все элементы, ID которых превышает заданный.
     * В случае ошибки (например, если аргумент не указан или имеет неверный формат) выводит соответствующее сообщение.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            result = new ArrayList<>();
            if (arguments.length != 1) {
                throw new CannotExecuteCommandException("Введены неверные аргументы команды");
            } else if (!RouteFieldValidation.validate("Id",arguments[0])){
                throw new CannotExecuteCommandException("Введены невалидные аргументы команды: Id = " + arguments[0]);
            }
            result.add(Integer.parseInt(arguments[0]));
        } else if (invocationStatus.equals((InvocationStatus.SERVER))){
            Integer id = (Integer) this.getResult().get(0);
            if (collectionManager.containsIdRoute(id)){
                collectionManager.removeGreater(id);
                printStream.println("Элементы с id > " + id + " удалены");
            }else printStream.println("Элемента с указанным id не существует");
        }
    }


    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда удаляет из коллекции все элементы,
     * ID которых превышает заданный.
     */
    @Override
    public String getDescription() {
        return "удаляет из коллекции все элементы, ID которых превышает заданный";
    }

}
