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
     *
     * @param commandInvoker инвокер, используемый для выполнения команд
     */
    public ClientCommandProcess(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    /**
     * Выполняет команду, указанную строкой командной строки.
     *
     * @param firstCommandLine строка командной строки для выполнения
     * @return true, если команда была успешно выполнена и не является командой "help", false в противном случае
     */
    public boolean executeCommand(String firstCommandLine) {
        if (!commandInvoker.executeClient(firstCommandLine, System.out)) {
            rootLogger.warn("Команда не была исполнена: {}", firstCommandLine);
            return false;
        } else {
            return !commandInvoker.getLastCommandContainer().getName().equals("help");
        }
    }
}
