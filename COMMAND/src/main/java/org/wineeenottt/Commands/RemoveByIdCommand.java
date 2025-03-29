package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;
import org.wineeenottt.Utility.RouteFieldValidation;

import java.io.PrintStream;
import java.util.ArrayList;

public class RemoveByIdCommand extends Command{

    private static final String FILE = "FILES/RouteStorage";
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;

    public RemoveByIdCommand() {
        super("remove_by_id");
    }
    /**
     * Конструктор класса RemoveByIdCommand.
     */
    public RemoveByIdCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.CLIENT)) {
            result = new ArrayList<>();
            if (arguments.length != 1) {
                throw new CannotExecuteCommandException("Количество аргументов у данной команды равно 1.");
            }
            if (RouteFieldValidation.validate("Id", arguments[0])) {
                result.add(Integer.parseInt(arguments[0])); //сохраняем id, меньше которых следует удалять.
            } else {
                throw new CannotExecuteCommandException("Введены невалидные аргументы: id = " + arguments[0]);
            }
        } else if (invocationEnum.equals(InvocationStatus.SERVER)) {
            Integer id = (Integer) this.getResult().get(0);
            collectionManager.removeById(id);
            collectionManager.save(FILE);
            printStream.println("Элементы с id = " + id + " были удалены");
        }
    }

    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "удаляет элемент с указанным ID";
    }
}

