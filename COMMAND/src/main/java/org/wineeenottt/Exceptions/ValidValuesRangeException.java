package org.wineeenottt.Exceptions;

/**
 * Исключение ValidValuesRangeException выбрасывается при обнаружении значения,
 * выходящего за допустимые пределы.
 * Наследуется от класса RuntimeException, что делает его необязательным для обработки.
 */
public class ValidValuesRangeException extends RuntimeException {

    /**
     * Конструктор класса ValidValuesRangeException.
     * Создает исключение без сообщения об ошибке.
     */
    public ValidValuesRangeException() {
        super();
    }
}
