package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Collection.Coordinates;
import org.wineeenottt.Collection.Location;
import org.wineeenottt.Collection.Route;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;
import org.wineeenottt.IO.UserIO;
import org.wineeenottt.Utility.RouteFieldValidation;
import org.wineeenottt.Utility.RouteFieldsReader;

import java.io.PrintStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Класс AddIfMaxCommand реализует интерфейс Command и представляет команду добавления нового элемента в коллекцию,
 * если его ID больше максимального ID существующих элементов в коллекции.
 * Команда использует RouteFieldsReader для чтения данных о маршруте и UserIO для взаимодействия с пользователем.
 */
public class AddIfMaxCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для управления коллекцией и проверки условий добавления элемента.
     */
    private CollectionManager collectionManager;

    /**
     * Поле, хранящее ссылку на объект RouteFieldsReader.
     * Используется для чтения данных о маршруте из указанного потока ввода.
     */
    private  RouteFieldsReader routeFieldsReader;

    /**
     * Поле, хранящее ссылку на объект UserIO.
     * Используется для взаимодействия с пользователем (вывод ошибок и запросов).
     */
    private UserIO userIO;

    /**
     * Конструктор класса AddIfMaxCommand.
     *объект класса UserIO, используемый для взаимодействия с пользователем.
     */
    public AddIfMaxCommand(RouteFieldsReader routeFieldsReader){
        super("add_if_max");
        this.routeFieldsReader = routeFieldsReader;
    }

    public AddIfMaxCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду. Проверяет, существует ли элемент с указанным ID,
     * и добавляет новый элемент в коллекцию, если его ID больше максимального ID существующих элементов.
     * В случае ошибки (например, если ID уже существует или меньше максимального) выводит соответствующее сообщение.
     */
            @Override
            public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
                if (invocationStatus.equals(InvocationStatus.CLIENT)) {
                    if (arguments.length > 0) {
                        throw new CannotExecuteCommandException("У данной команды нет аргументов");
                    }

                    // Собираем данные о маршруте на клиенте
                    result = new ArrayList<>();
                    result.add(routeFieldsReader.readInt("ID (Integer > 0): "));
                    result.add(routeFieldsReader.readName());
                    result.add(routeFieldsReader.readCoordinates());
                    result.add(ZonedDateTime.now());
                    result.add(routeFieldsReader.readLocation());
                    result.add(routeFieldsReader.readLocation());
                    result.add(routeFieldsReader.readDistance());

                } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
                    if (result == null || result.size() != 7) {
                        throw new CannotExecuteCommandException("Некорректные данные для добавления маршрута");
                    }

                    Integer id = (Integer) result.get(0);
                    String name = (String) result.get(1);
                    Coordinates coordinates = (Coordinates) result.get(2);
                    ZonedDateTime creationDate = (ZonedDateTime) result.get(3);
                    Location fromLocation = (Location) result.get(4);
                    Location toLocation = (Location) result.get(5);
                    Long distance = (Long) result.get(6);

                    collectionManager.addIfMaxIdRoute(id,name, coordinates, creationDate, fromLocation, toLocation, distance);
                    printStream.println("Маршрут добавлен");}}
//            if (id > collectionManager.getMaxId()) {
//                collectionManager.addIfMaxIdRoute(id,name, coordinates, creationDate, fromLocation, toLocation, distance);
//                printStream.println("Маршрут добавлен");
//            } else {
//                printStream.println("ID должен быть больше максимального существующего ID");
//            }
//        } else {
//            throw new CannotExecuteCommandException("Неизвестный статус выполнения команды");
//        }


    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда добавляет элемент в коллекцию,
     * если его ID больше максимального ID существующих элементов.
     */
    @Override
    public String getDescription() {
        return "добавляет элемент в коллекцию, если его ID больше максимального ID существующих элементов";
    }
}
