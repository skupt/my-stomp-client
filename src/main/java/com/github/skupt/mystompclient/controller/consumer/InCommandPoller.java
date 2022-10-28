package com.github.skupt.mystompclient.controller.consumer;

import com.github.skupt.mystompclient.commands.StompCommand;
import com.github.skupt.mystompclient.service.ReceiveQueService;

public class InCommandPoller implements Runnable {
    public volatile boolean stop = false;
    private ReceiveQueService inQueueService;
    private CommandConsumerCallback commandConsumer;

    public InCommandPoller(ReceiveQueService inQueueService, CommandConsumerCallback consumerCallback) {
        this.inQueueService = inQueueService;
        this.commandConsumer = consumerCallback;
    }

    @Override
    public void run() {
        while (!stop) {
            StompCommand stompCommand = inQueueService.pollFirstCommand();
            commandConsumer.onInCommand(stompCommand);
        }
    }
}
