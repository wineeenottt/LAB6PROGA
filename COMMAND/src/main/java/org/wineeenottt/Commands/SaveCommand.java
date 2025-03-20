package org.wineeenottt.Commands;

import org.wineeenottt.Collection.CollectionManager;
import org.wineeenottt.Exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class SaveCommand extends Command {

    /**
     * Поле, хранящее адрес файла, куда следует сохранить коллекцию.
     */
    private String inputFile;

    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     * Используется для доступа к методам управления коллекцией и её сохранения.
     */
    private CollectionManager collectionManager;

    /**
     * Конструктор класса SaveCommand.
     *
     * @param collectionManager объект класса CollectionManager, используемый для управления коллекцией.
     * @param inputFile         строка, содержащая адрес файла, куда следует сохранить коллекцию.
     */
    public SaveCommand(CollectionManager collectionManager, String inputFile) {
        this.collectionManager = collectionManager;
        this.inputFile = inputFile;
    }

    /**
     * Метод, выполняющий команду. Сохраняет коллекцию в указанный файл.
     * В случае успешного сохранения выводит сообщение об успешном выполнении.
     * В случае ошибки выводит соответствующее сообщение.
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            throw new CannotExecuteCommandException("У данной команды для клиента нет выполнения");
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            collectionManager.save(inputFile);
            printStream.println("Коллекция " + collectionManager.getClass().getSimpleName() + " была сохранена");
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return строка с описанием команды, указывающая, что команда сохраняет коллекцию в указанный файл.
     */
    @Override
    public String getDescription() {
        return "сохраняет коллекцию в указанный файл";
    }
}
