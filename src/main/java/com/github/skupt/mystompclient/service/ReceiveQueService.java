package com.github.skupt.mystompclient.service;

import com.github.skupt.mystompclient.commands.StompCommand;

import java.util.concurrent.LinkedBlockingQueue;

public class ReceiveQueService {
    private static long maxQueLength = 25000;
    private LinkedBlockingQueue<StompCommand> receivedQueue = new LinkedBlockingQueue<>();

    public void addLastCommand(StompCommand command) {
        receivedQueue.add(command);
    }

    public StompCommand pollFirstCommand() {
        throw new UnsupportedOperationException();
    }
}
