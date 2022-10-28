package com.github.skupt.mystompclient.controller.consumer;

import com.github.skupt.mystompclient.commands.StompCommand;
import com.github.skupt.mystompclient.service.SentQueueService;

public class OutCommandPoller implements Runnable {
    public volatile boolean stop = false;
    private SentQueueService sentQueueService;
    private CommandConsumerCallback handlerCallback;

    public OutCommandPoller(SentQueueService sentQueueService, CommandConsumerCallback consumerCallback) {
        this.sentQueueService = sentQueueService;
        this.handlerCallback = consumerCallback;
    }

    @Override
    public void run() {
        while (!stop) {
            StompCommand stompCommand = sentQueueService.pollFirstCommand();
            handlerCallback.onOutCommand(stompCommand);
        }
    }
}
