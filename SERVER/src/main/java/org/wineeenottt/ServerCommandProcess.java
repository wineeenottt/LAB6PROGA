package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;
import org.wineeenottt.Commands.CommandInvoker;

import java.io.PrintStream;

/**
 * Класс ServerCommandProcess отвечает за обработку и выполнение команд, полученных от клиента.
 * Он использует объект CommandInvoker для выполнения команд и логирует процесс выполнения.
 */
public class ServerCommandProcess {
    private final CommandInvoker commandInvoker;

    private static final Logger rootLogger = LoggerFactory.getLogger(ServerCommandProcess.class);

    /**
     * Конструктор класса ServerCommandProcess.
     * Инициализирует объект CommandInvoker, который будет использоваться для выполнения команд.
     *
     * @param commandInvoker объект CommandInvoker для выполнения команд
     */
    public ServerCommandProcess(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    /**
     * Выполняет команду, полученную от клиента.
     * Метод логирует полученную команду, передает её на выполнение в CommandInvoker
     * и логирует результат выполнения.
     *
     * @param command      объект CommandContainer, содержащий команду и её данные
     * @param printStream  поток вывода для отправки результатов выполнения команды клиенту
     */
    public void executeCommand(CommandContainer command, PrintStream printStream) {
        rootLogger.info("Получена команда: {}", command.getName());
        if (commandInvoker.executeServer(command.getName(), command.getResult(), printStream)) {
            rootLogger.info("Была исполнена команда {}", command.getName());
        } else {
            rootLogger.info("Не была исполнена команда {}", command.getName());
        }
    }
}