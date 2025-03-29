package org.wineeenottt.Commands;


import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;
import org.wineeenottt.IO.UserIO;
import org.wineeenottt.Utility.RouteFieldValidation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class UpdateElementCommand extends Command{
    private static final String FILE = "FILES/RouteStorage";
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;
    /**
     * Поле, хранящее ссылку на объект класса UserIO.
     */
    private UserIO userIO;
    /**
     * Конструктор класса с единственным аргументом UserIO
     */
    public UpdateElementCommand(UserIO userIO){
        super("update");
        this.userIO=userIO;
    }
    /**
     * Конструктор класса с единственным аргументом CollectionManager
     */
    public UpdateElementCommand(CollectionManager collectionManager){
        this.collectionManager=collectionManager;
    }

//    public UpdateElementCommand(CollectionManager collectionManager,UserIO userIO){
//        this.collectionManager=collectionManager;
//        this.userIO=userIO;
//    }
    /**
     * Метод, исполняющий команду. При вызове изменяется указанной элемент коллекции до тех пор, пока не будет передана пустая строка. В случае некорректного ввода высветится ошибка.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.CLIENT)) {
            result = new ArrayList<>();
            try {
                if (arguments.length != 1) {
                    throw new CannotExecuteCommandException("Количество аргументов данной команды должно равняться 1.");
                }
                if (!RouteFieldValidation.validate("Id", arguments[0])) {
                    throw new CannotExecuteCommandException("Введены невалидные аргументы: id =" + arguments[0]);
                } else {
                    result.add(arguments[0]);
                    printStream.println(CollectionManager.getFieldNames());
                    printStream.println("\nВыберите поля для изменения (введите 'stop' для завершения):");
                    String[] line;

                    boolean isInputEnd = false;

                    do {
                        line = userIO.readLine().trim().split("\\s+");
                        if (line.length == 0 || line[0] == null || line[0].equals("") || line[0].equalsIgnoreCase("stop")) {
                            isInputEnd = true;
                        } else {
                            if (line.length == 1) {
                                if (RouteFieldValidation.validate(line[0], "")) {
                                    result.add(line[0] + ";");
                                } else {
                                    printStream.println("Введены некорректные данные: \"" + line[0] + "\" + null");
                                }
                            }
                            if (line.length == 2) {
                                if (RouteFieldValidation.validate(line[0], line[1])) {
                                    result.add(line[0] + ";" + line[1]);
                                } else {
                                    printStream.println("Введены некорректные данные: " + line[0] + " + " + line[1]);
                                }
                            }
                        }
                    } while (!isInputEnd);
                }
            } catch (NoSuchElementException ex) {
                throw new CannotExecuteCommandException("Сканнер достиг конца файла.");
            }
        } else if (invocationEnum.equals(InvocationStatus.SERVER)) {
            String[] spArguments = result.toArray(new String[0]);
            Integer id = Integer.parseInt(spArguments[0]);
            if (collectionManager.containsIdRoute(id)) {
                for (int i = 1; i < spArguments.length; i++) {
                    String[] subStr;
                    String delimeter = ";";
                    subStr = spArguments[i].split(delimeter);
                    collectionManager.update(id, subStr[0], subStr[1], printStream);
                    collectionManager.save(FILE);
                }
                printStream.println("Указанные поля были заменены.");
            } else {
                printStream.println("Элемента с указанным id не существует");
            }
        }
    }
    /**
     * Метод, возвращающий описание команды.
     */
    @Override
    public String getDescription() {
        return "изменяет указанное поле выбранного по id элемента коллекции";
    }
}
