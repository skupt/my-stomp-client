package com.github.skupt.mystompclient.service;

import com.github.skupt.mystompclient.commands.Command;
import com.github.skupt.mystompclient.commands.StompCommand;

import java.util.concurrent.LinkedBlockingQueue;

public class ReceiveQueService {
    private static long maxQueLength = 25000;
    private LinkedBlockingQueue<StompCommand> inQue = new LinkedBlockingQueue<>();

    public synchronized void addLastCommand(StompCommand command) {
        inQue.add(command);
        notify();
    }

    public synchronized StompCommand pollFirstCommand() {
        StompCommand stompCommand = null;
        try {
            while (inQue.isEmpty()) wait();
            stompCommand = inQue.take();
            notify();
        } catch (InterruptedException e) {
            stompCommand = new StompCommand(Command.STOP_MY_STOMP_PROCESSING, null, null);
            Thread.currentThread().interrupt();
        }
        return stompCommand;
    }
}
