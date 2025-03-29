package org.wineeenottt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wineeenottt.Commands.CommandInvoker;

/**
 * Класс ClientCommandProcess отвечает за обработку и выполнение команд клиента.
 * Он использует CommandInvoker для выполнения команд.
 */
public class ClientCommandProcess {
    private static final Logger rootLogger = LoggerFactory.getLogger(ClientCommandProcess.class);
    private final CommandInvoker commandInvoker;

    /**
     * Создает ClientCommandProcess с указанным CommandInvoker.
     */
    public ClientCommandProcess(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    /**
     * Выполняет команду, указанную строкой командной строки.
     */
    public boolean executeCommand(String firstCommandLine) {
        if (!commandInvoker.executeClient(firstCommandLine, System.out)) {
            rootLogger.warn("Команда не была исполнена клиентом: {}", firstCommandLine);
            return true;
        } else {
            return commandInvoker.getLastCommandContainer().getName().equals("help");
        }
    }
}
