package com.github.skupt.mystompclient.controller.consumer;

import com.github.skupt.mystompclient.StompReceiver;
import com.github.skupt.mystompclient.commands.Command;
import com.github.skupt.mystompclient.commands.StompCommand;
import com.github.skupt.mystompclient.controller.producer.CommandProducer;
import com.github.skupt.mystompclient.service.SentQueueService;
import lombok.Data;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Data
public class CommandConsumer implements CommandConsumerCallback {
    public static Logger logger = Logger.getLogger(CommandConsumer.class.getName());

    static {
        logger.setLevel(CommandProducer.loggerLevel);
    }

    private SentQueueService sentQueueService;
    private PrintStream printStream;
    private StompReceiver stompReceiver;

    public CommandConsumer(SentQueueService sentQueueService, PrintStream printStream) {
        this.sentQueueService = sentQueueService;
        this.printStream = printStream;
    }

    ;

    public void onOutCommand(StompCommand command) {
        // handle CONNECT
        if (command.getCommand() == Command.CONNECT) {
            printStream.print(command);
            String cmdStr = command.toString();
            logger.info(cmdStr);
            while (!sentQueueService.clientConnected) {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // handle CONNECTED
        if (command.getCommand() == Command.CONNECTED) {
            throw new UnsupportedOperationException();
        }
        // handle SEND
        if (command.getCommand() == Command.SEND) {
            printStream.print(command);
            String cmdStr = command.toString();
            logger.info(cmdStr);
        }
    }

    public void onInCommand(StompCommand command) {
        logger.info(command.toString());
        stompReceiver.onMessage(command);

    }

    public CommandConsumerCallback commandConsumerCallback() {
        return this;
    }
}
