package com.github.skupt.mystompclient.receiver;

import com.github.skupt.mystompclient.StompClient;
import com.github.skupt.mystompclient.commands.StompCommand;
import lombok.Data;

import java.util.logging.Logger;

@Data
public class DefaultStompReceiver implements StompReceiver {
    private static final Logger logger = Logger.getLogger(DefaultStompReceiver.class.getName());

    static {
        logger.setLevel(StompClient.loggerLevel);
    }

//    private StompCommand lastReceivedCommand;

    @Override
    public void onMessage(StompCommand stompCommand) {
        logger.log(logger.getLevel(), stompCommand.toString());
    }
}
