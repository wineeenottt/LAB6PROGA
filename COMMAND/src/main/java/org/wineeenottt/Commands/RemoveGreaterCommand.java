package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;
import org.wineeenottt.Utility.RouteFieldValidation;

import java.io.PrintStream;
import java.util.ArrayList;

public class RemoveGreaterCommand extends Command {
    private static final String FILE = "FILES/RouteStorage";

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;


    public RemoveGreaterCommand(){
        super("remove_greater");
    }
    /**
     * Конструктор класса RemoveGreaterCommand.
     */
    public RemoveGreaterCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду.
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
                collectionManager.save(FILE);
                printStream.println("Элементы с id > " + id + " удалены");
            }else printStream.println("Элемента с указанным id не существует");
        }
    }


    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "удаляет из коллекции все элементы, ID которых превышает заданный";
    }

}
