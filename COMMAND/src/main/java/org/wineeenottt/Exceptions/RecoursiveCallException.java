package org.wineeenottt.Exceptions;

/**
 * Исключение RecoursiveCallException выбрасывается при обнаружении рекурсивного вызова.
 * Наследуется от класса RuntimeException, что делает его необязательным для обработки.
 */
public class RecoursiveCallException extends RuntimeException {

    /**
     * Конструктор класса RecoursiveCallException.
     *
     * @param s сообщение, которое будет передано в исключение.
     */
    public RecoursiveCallException(String s) {
        super(s);
    }
}
