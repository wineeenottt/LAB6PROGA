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

public class AddIfMaxCommand extends Command {
    private static final String FILE = "FILES/RouteStorage";
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;

    /**
     * Поле, хранящее ссылку на объект RouteFieldsReader.
     */
    private  RouteFieldsReader routeFieldsReader;

    /**
     * Поле, хранящее ссылку на объект UserIO.
     */
    private UserIO userIO;

    /**
     * Конструктор класса AddIfMaxCommand.
     */
    public AddIfMaxCommand(RouteFieldsReader routeFieldsReader){
        super("add_if_max");
        this.routeFieldsReader = routeFieldsReader;
    }

    public AddIfMaxCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, выполняющий команду.
     */
            @Override
            public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
                if (invocationStatus.equals(InvocationStatus.CLIENT)) {
                    if (arguments.length > 0) {
                        throw new CannotExecuteCommandException("У данной команды нет аргументов");
                    }

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
                    collectionManager.save(FILE);
                    printStream.println("Маршрут добавлен");}}



    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "добавляет элемент в коллекцию, если его ID больше максимального ID существующих элементов";
    }
}
