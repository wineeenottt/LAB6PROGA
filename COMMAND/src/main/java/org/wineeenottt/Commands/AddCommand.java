package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Collection.Coordinates;
import org.wineeenottt.Collection.Location;
import org.wineeenottt.Collection.Route;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;
import org.wineeenottt.Utility.RouteFieldsReader;

import java.io.PrintStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Класс AddCommand реализует интерфейс Command и представляет команду добавления нового элемента в коллекцию.
 * Команда использует RouteFieldsReader для чтения данных о маршруте и добавляет новый элемент в коллекцию через CollectionManager.
 */
public class AddCommand extends Command {

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для управления коллекцией и добавления нового элемента.
     */
    private CollectionManager collectionManager;

    /**
     * Поле, хранящее ссылку на объект RouteFieldsReader.
     * Используется для чтения данных о маршруте из указанного потока ввода.
     */
    private RouteFieldsReader routeFieldsReader;

    public AddCommand(RouteFieldsReader routeFieldsReader){
        super("add");
        this.routeFieldsReader = routeFieldsReader;
    }
    /**
     * Конструктор класса AddCommand.
     *
     * @param collectionManager объект класса CollectionManager, используемый для управления коллекцией.
     */
    public AddCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду. Читает данные о маршруте с помощью RouteFieldsReader
     * и добавляет новый элемент в коллекцию через CollectionManager.
     * После успешного добавления выводит сообщение об успешном выполнении.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            }
            if (routeFieldsReader == null) {
                throw new CannotExecuteCommandException("RouteFieldsReader не инициализирован");
            }

            // Собираем данные о маршруте на клиенте
            result = new ArrayList<>();
            result.add(routeFieldsReader.readName());           // String
            result.add(routeFieldsReader.readCoordinates());   // Coordinates
            result.add(ZonedDateTime.now());                   // ZonedDateTime
            result.add(routeFieldsReader.readLocation());      // Location (from)
            result.add(routeFieldsReader.readLocation());      // Location (to)
            result.add(routeFieldsReader.readDistance());      // Long

        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            if (result == null || result.size() != 6) {
                throw new CannotExecuteCommandException("Некорректные данные для добавления маршрута");
            }

            // Извлекаем данные из result
            String name = (String) result.get(0);
            Coordinates coordinates = (Coordinates) result.get(1);
            ZonedDateTime creationDate = (ZonedDateTime) result.get(2);
            Location fromLocation = (Location) result.get(3);
            Location toLocation = (Location) result.get(4);
            Long distance = (Long) result.get(5);

            // Добавляем маршрут в коллекцию
            collectionManager.addRoute(name, coordinates, creationDate, fromLocation, toLocation, distance);
            printStream.println("Маршрут добавлен");
        } else {
            throw new CannotExecuteCommandException("Неизвестный статус выполнения команды");
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда добавляет элемент в коллекцию.
     */
    @Override
    public String getDescription() {
        return "добавляет элемент в коллекцию";
    }
}
