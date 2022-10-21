package com.github.skupt.mystompclient.service;

import com.github.skupt.mystompclient.MyStompClient;
import com.github.skupt.mystompclient.commands.Command;
import com.github.skupt.mystompclient.commands.StompCommand;
import lombok.Data;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Data
public class CommandService {
    public static Logger logger = Logger.getLogger(CommandService.class.getName());
    static {
        logger.setLevel(MyStompClient.loggerLevel);
    }
    SentQueueService sentQueueService;
    PrintStream printStream;
    public void handleOutCommand() {
        StompCommand command = sentQueueService.pollFirstCommand();
        // handle CONNECT
        if (command.getCommand() == Command.CONNECT) {
            printStream.print(command);
            String cmdStr = command.toString();
            logger.info(cmdStr);
            while(!sentQueueService.clientConnected) {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // handle CONNECTED
        if (command.getCommand() == Command.CONNECTED)
        // handle SEND
        if (command.getCommand() == Command.SEND) {
            printStream.print(command);
            String cmdStr = command.toString();
            logger.info(cmdStr);
        }

    }

    public void handleInCommand() {

    }
}
