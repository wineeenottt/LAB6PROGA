package org.wineeenottt.IO;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс UserIO предоставляет методы для взаимодействия с пользователем через консоль.
 * Он позволяет считывать ввод пользователя, выводить текстовые сообщения, сообщения об ошибках
 * и преамбулу для запроса ввода.
 */
public class UserIO {
    /**
     * Поле хранит ссылку на объект Scanner, который используется для чтения данных из указанного места.
     */
    Scanner scanner;

    /**
     * Конструктор класса без параметров. Инициализирует объект Scanner для чтения данных
     * из стандартного потока ввода (System.in) с использованием кодировки UTF-8.
     */
    public UserIO() {
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    /**
     * Конструктор класса с параметром. Позволяет передать пользовательский объект Scanner
     * для чтения данных из произвольного источника.
     *
     * @param scanner объект Scanner, который будет использоваться для чтения данных.
     */
    public UserIO(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Метод для чтения строки данных из источника, указанного в объекте Scanner.
     *
     * @return строка, считанная из источника.
     */
    public String readLine() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            throw new NoSuchElementException("Ввод пустой или поток ввода был закрыт.");
        }
    }

    /**
     * Метод для вывода текста команды в стандартный поток вывода (System.out).
     *
     * @param str строка, которую необходимо вывести.
     */
    public void printCommandText(String str) {
        System.out.println(str);
    }

    /**
     * Метод для вывода текста ошибки команды в стандартный поток вывода ошибок (System.err).
     *
     * @param str строка, содержащая сообщение об ошибке.
     */
    public void printCommandError(String str) {
        System.err.println(str);
    }

    /**
     * Метод для вывода символа стрелки вправо (">") перед запросом ввода команды.
     * Используется для визуального обозначения запроса ввода.
     */
    public void printPreamble() {
        System.out.print(">");
    }
}