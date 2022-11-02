package com.github.skupt.mystompclient.controller.consumer;

import com.github.skupt.mystompclient.StompClient;
import com.github.skupt.mystompclient.commands.Command;
import com.github.skupt.mystompclient.commands.StompCommand;
import com.github.skupt.mystompclient.receiver.StompReceiver;
import com.github.skupt.mystompclient.service.SentQueueService;
import lombok.Data;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Data
public class CommandConsumer implements CommandConsumerCallback {
    public static Logger logger = Logger.getLogger(CommandConsumer.class.getName());

    static {
        logger.setLevel(StompClient.loggerLevel);
    }

    private SentQueueService sentQueueService;
    private PrintStream printStream;
    private Set<StompReceiver> stompReceivers = new HashSet<>();

    public CommandConsumer(SentQueueService sentQueueService, PrintStream printStream) {
        this.sentQueueService = sentQueueService;
        this.printStream = printStream;
    }

    public volatile boolean clientConnected = false;

    public void onOutCommand(StompCommand command) {
        if (command == null) {
            logger.log(logger.getLevel(), "Command is null in CommandConsumer.onOutCommand(null)");
            return;
        }
        if (command.getCommand() == Command.STOP_MY_STOMP_PROCESSING) return;
        logger.log(logger.getLevel(), command.toString());
        printStream.print(command);
    }

    public void onInCommand(StompCommand command) {
        if (command.getCommand() == Command.STOP_MY_STOMP_PROCESSING) return;
        stompReceivers.forEach(c -> c.onMessage(command));
    }

    public void addStompReceiver(StompReceiver stompReceiver) {
        stompReceivers.add(stompReceiver);
    }

    public CommandConsumerCallback commandConsumerCallback() {
        return this;
    }
}
