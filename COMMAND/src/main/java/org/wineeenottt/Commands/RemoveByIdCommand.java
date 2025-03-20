package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;
import org.wineeenottt.Utility.RouteFieldValidation;

import java.io.PrintStream;
import java.util.ArrayList;

public class RemoveByIdCommand extends Command{

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для управления коллекцией и удаления элемента по ID.
     */
    private CollectionManager collectionManager;

    public RemoveByIdCommand() {
        super("remove_by_id");
    }
    /**
     * Конструктор класса RemoveByIdCommand.
     *
     * @param collectionManager объект класса CollectionManager, используемый для управления коллекцией.
     */
    public RemoveByIdCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду. Удаляет элемент коллекции по указанному ID.
     * В случае ошибки (например, если аргумент не указан или имеет неверный формат) выводит соответствующее сообщение.
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
            printStream.println("Элементы с id = " + id + " были удалены");
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда удаляет элемент с указанным ID.
     */
    @Override
    public String getDescription() {
        return "удаляет элемент с указанным ID";
    }
}

