package org.wineeenottt.Utility;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Collection.Coordinates;
import org.wineeenottt.Collection.Location;
import org.wineeenottt.Exceptions.ValidValuesRangeException;
import org.wineeenottt.IO.UserIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Класс, предназначенный для чтения и валидации полей маршрута (Route).
 * Поддерживает чтение данных как из файла, так и из пользовательского ввода.
 */
public class RouteFieldsReader {

    /**
     * Объект для взаимодействия с пользователем (ввод/вывод).
     */
    private final UserIO userIO;

    /**
     * Менеджер коллекции, используемый для работы с данными.
     */
    private CollectionManager collectionManager;

    /**
     * Сканер для чтения данных из файла.
     */
    private Scanner scanner;

    /**
     * Массив строк, содержащий данные, считанные из файла.
     */
    private String[] inputDataArray;

    /**
     * Индекс текущего элемента в массиве inputDataArray.
     */
    private int inputIndex = 0;

    /**
     * Конструктор класса RouteFieldsReader.
     *
     * @param userIO объект для взаимодействия с пользователем.
     */
    public RouteFieldsReader(UserIO userIO) {
        this.userIO = userIO;
    }

    /**
     * Устанавливает файл для чтения данных.
     *
     * @param fileName имя файла, из которого будут читаться данные.
     */
    public void setInputData(String fileName) {
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось открыть файл: " + fileName);
            scanner = null;
        }
        inputDataArray = null;
        inputIndex = 0;
    }

    /**
     * Читает следующее значение из файла или запрашивает его у пользователя.
     *
     * @param val сообщение, которое будет выведено пользователю, если данные читаются с консоли.
     * @return следующее значение.
     */
    private String readNextValue(String val) {
        while ((inputDataArray == null || inputIndex >= inputDataArray.length) && scanner != null && scanner.hasNextLine()) {
            inputDataArray = scanner.nextLine().trim().split(",");
            inputIndex = 0;
        }

        if (inputDataArray != null && inputIndex < inputDataArray.length) {
            return inputDataArray[inputIndex++].trim();
        }

        userIO.printCommandText(val);
        return userIO.readLine().trim();
    }

    /**
     * Читает и валидирует имя маршрута.
     *
     * @return валидное имя маршрута.
     */
    public String readName() {
        while (true) {
            String str = readNextValue("Name (not null): ");
            if (!str.isEmpty()) return str;
            userIO.printCommandError("Значение поля не может быть null или пустой строкой\n");
        }
    }

    /**
     * Читает и создает объект Coordinates.
     *
     * @return объект Coordinates.
     */
    public Coordinates readCoordinates() {
        return new Coordinates(readCoordinateX(), readCoordinateY());
    }

    /**
     * Читает и валидирует координату X.
     *
     * @return валидное значение координаты X.
     */
    public Double readCoordinateX() {
        while (true) {
            try {
                double x = Double.parseDouble(readNextValue("CoordinateX (Double & x <= 750): "));
                /**
                 * Максимальное допустимое значение координаты X.
                 */
                double MAX = 750;
                if (x > MAX) throw new ValidValuesRangeException();
                return x;
            } catch (ValidValuesRangeException e) {
                System.out.println("Координата x имеет максимальное значение - 750");
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа Double");
            }
        }
    }

    /**
     * Читает и валидирует координату Y.
     *
     * @return валидное значение координаты Y.
     */
    public float readCoordinateY() {
        return readFloat("CoordinateY (Float): ");
    }

    /**
     * Читает и создает объект Location.
     *
     * @return объект Location.
     */
    public Location readLocation() {
        return new Location(readLocationCoordinateX(), readLocationCoordinateY(), readLocationCoordinateZ(), readLocationName());
    }

    /**
     * Читает и валидирует координату X для Location.
     *
     * @return валидное значение координаты X.
     */
    public float readLocationCoordinateX() {
        return readFloat("Location coordinateX (Float): ");
    }

    /**
     * Читает и валидирует координату Y для Location.
     *
     * @return валидное значение координаты Y.
     */
    public int readLocationCoordinateY() {
        return readInt("Location coordinateY (Int): ");
    }

    /**
     * Читает и валидирует координату Z для Location.
     *
     * @return валидное значение координаты Z.
     */
    public double readLocationCoordinateZ() {
        return readDouble("Location coordinateZ (Double): ");
    }

    /**
     * Читает и валидирует имя для Location.
     *
     * @return валидное имя Location.
     */
    public String readLocationName() {
        while (true) {
            String str = readNextValue("LocationName (not null): ");
            if (!str.isEmpty()) return str;
            userIO.printCommandError("Значение поля не может быть null или пустой строкой\n");
        }
    }

    /**
     * Читает и валидирует расстояние (Distance).
     *
     * @return валидное значение расстояния.
     */
    public long readDistance() {
        while (true) {
            try {
                long distance = Long.parseLong(readNextValue("Distance (Long > 1): "));
                long MIN = 1;
                if (distance <= MIN) throw new ValidValuesRangeException();
                return distance;
            } catch (ValidValuesRangeException e) {
                System.out.println("Distance должно быть больше 1");
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа Long");
            }
        }
    }

    /**
     * Читает и валидирует значение типа Float.
     *
     * @param val сообщение для пользователя.
     * @return валидное значение типа Float.
     */
    private float readFloat(String val) {
        while (true) {
            try {
                return Float.parseFloat(readNextValue(val));
            } catch (NumberFormatException e) {
                System.err.println("Ввод должен быть типа Float");
            }
        }
    }

    /**
     * Читает и валидирует значение типа Double.
     *
     * @param val сообщение для пользователя.
     * @return валидное значение типа Double.
     */
    private double readDouble(String val) {
        while (true) {
            try {
                return Double.parseDouble(readNextValue(val));
            } catch (NumberFormatException e) {
                System.err.println("Ввод должен быть типа Double");
            }
        }
    }

    /**
     * Читает и валидирует значение типа Integer.
     *
     * @param val сообщение для пользователя.
     * @return валидное значение типа Integer.
     */
    public int readInt(String val) {
        while (true) {
            try {
                return Integer.parseInt(readNextValue(val));
            } catch (NumberFormatException e) {
                System.err.println("Ввод должен быть типа Integer");
            }
        }
    }
    public int readId(String val) {
        while (true) {
            try {
                return Integer.parseInt(readNextValue(val));
            } catch (NumberFormatException e) {
                System.err.println("Ввод должен быть типа Integer");
            }
        }
    }
}