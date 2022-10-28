package com.github.skupt.mystompclient.service;

import com.github.skupt.mystompclient.commands.Command;
import com.github.skupt.mystompclient.commands.StompCommand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class SentQueueService {

    private static long maxQueLength = 25000;

    private Set<String> destinationToAck = new HashSet<>();
    private LinkedBlockingQueue<StompCommand> outQue = new LinkedBlockingQueue<>();
    private Map<String, StompCommand> toReceiptMap = new HashMap<>();

    public boolean empty = true;

    public synchronized void addLastOutCommand(StompCommand stompCommand) {
        if (stompCommand.getCommand() == Command.CONNECT) {
            addAndNotify(stompCommand);
            return;
        }
        if (stompCommand.getCommand() == Command.SEND) {
            String destination = stompCommand.getHeaders().get("destination");
            // handle destination header
            if (destination != null) {
                outQue.add(stompCommand);
            } else {
                throw new RuntimeException("Command SEND must have not null destination header. Command uuid: " + stompCommand.getUuid());
            }
            // handle receipt header
            String receipt = stompCommand.getHeaders().get("receipt");
            if (receipt != null) {
                toReceiptMap.put(stompCommand.getUuid(), stompCommand);
            }
            empty = false;
            notify();
            return;
        }
        if (stompCommand.getCommand() == Command.DISCONNECT) {
            addAndNotify(stompCommand);
            return;
        }
        // add new Command here

        // here default handler acts
        addAndNotify(stompCommand);

    }

    private void addAndNotify(StompCommand stompCommand) {
        outQue.add(stompCommand);
        empty = false;
        notify();
    }

    // todo refactor it like in receiveQueService and remove field empty
    public synchronized StompCommand pollFirstCommand() {
        StompCommand stompCommand = null;
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            stompCommand = outQue.take();
            empty = outQue.isEmpty() ? true : false;
            notify();
            return stompCommand;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new StompCommand(Command.STOP_MY_STOMP_PROCESSING, null, null);
    }


}
