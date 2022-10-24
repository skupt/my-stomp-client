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
    public volatile boolean clientConnected = false;


    public synchronized void addLastOutCommand(StompCommand stompCommand) {
        if (stompCommand.getCommand() == Command.SEND) {
            String destination = stompCommand.getHeaders().get("destination");
            // handle destination header
            if (destination != null) outQue.add(stompCommand);
            else
                throw new RuntimeException("Command SEND must have not null destination header. Command uuid: " + stompCommand.getUuid());
            // handle receipt header
            String receipt = stompCommand.getHeaders().get("receipt");
            if (receipt != null) toReceiptMap.put(stompCommand.getUuid(), stompCommand);
        }
    }

    public synchronized StompCommand pollFirstCommand() {
        return outQue.poll();
    }


}
