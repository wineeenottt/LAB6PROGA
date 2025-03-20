package org.wineeenottt.Exceptions;

/**
 * Исключение, выбрасываемое в случае, если команда не может быть исполнена в поданным ей аргументами.
 */
public class CannotExecuteCommandException extends Exception {

    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param mes сообщение, описывающее причину возникновения исключения.
     */
    public CannotExecuteCommandException(String mes) {
        super(mes);
    }
}
