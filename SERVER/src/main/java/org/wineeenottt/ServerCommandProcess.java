package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandContainer;
import org.wineeenottt.Commands.CommandInvoker;

import java.io.PrintStream;

/**
 * Класс ServerCommandProcess отвечает за обработку и выполнение команд, полученных от клиента.
 */
public class ServerCommandProcess {
    /**
     * Логгер для записи.
     */
    private static final Logger rootLogger = LoggerFactory.getLogger(ServerCommandProcess.class);

    private final CommandInvoker commandInvoker;

    /**
     * Конструктор класса ServerCommandProcess.
     */
    public ServerCommandProcess(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    /**
     * Выполняет команду, полученную от клиента.
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