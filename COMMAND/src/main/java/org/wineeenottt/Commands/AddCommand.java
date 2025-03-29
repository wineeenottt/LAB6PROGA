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

public class AddCommand extends Command {
    private static final String FILE = "FILES/RouteStorage";
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;

    /**
     * Поле, хранящее ссылку на объект RouteFieldsReader.
     */
    private RouteFieldsReader routeFieldsReader;

    public AddCommand(RouteFieldsReader routeFieldsReader){
        super("add");
        this.routeFieldsReader = routeFieldsReader;
    }
    /**
     * Конструктор класса AddCommand.
     */
    public AddCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду
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

            result = new ArrayList<>();
            result.add(routeFieldsReader.readName());
            result.add(routeFieldsReader.readCoordinates());
            result.add(ZonedDateTime.now());
            result.add(routeFieldsReader.readLocation());
            result.add(routeFieldsReader.readLocation());
            result.add(routeFieldsReader.readDistance());

        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            if (result == null || result.size() != 6) {
                throw new CannotExecuteCommandException("Некорректные данные для добавления маршрута");
            }


            String name = (String) result.get(0);
            Coordinates coordinates = (Coordinates) result.get(1);
            ZonedDateTime creationDate = (ZonedDateTime) result.get(2);
            Location fromLocation = (Location) result.get(3);
            Location toLocation = (Location) result.get(4);
            Long distance = (Long) result.get(5);


            collectionManager.addRoute(name, coordinates, creationDate, fromLocation, toLocation, distance);
            collectionManager.save(FILE);
            printStream.println("Маршрут добавлен");
        } else {
            throw new CannotExecuteCommandException("Неизвестный статус выполнения команды");
        }
    }

    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "добавляет элемент в коллекцию";
    }
}
