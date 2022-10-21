package com.github.skupt.mystompclient.service;

import com.github.skupt.mystompclient.commands.Command;
import com.github.skupt.mystompclient.commands.StompCommand;

import java.util.*;

public class SentQueueService {

    private Set<String> destinationToAck = new HashSet<>();
    private LinkedList<StompCommand> toGoList = new LinkedList<>();
    private Map<String, StompCommand> toReceiptMap = new HashMap<>();
    private volatile boolean toGoEmpty = true;
    public volatile boolean clientConnected = false;


    public void addLastOutCommand(StompCommand stompCommand) {
        if (stompCommand.getCommand() == Command.SEND) {
            String destination = stompCommand.getHeaders().get("destination");
            // handle destination header
            if (destination != null) toGoList.add(stompCommand);
            else throw new RuntimeException("Command SEND must have not null destination header. Command uuid: " + stompCommand.getUuid());
            // handle receipt header
            String receipt = stompCommand.getHeaders().get("receipt");
            if (receipt != null) toReceiptMap.put(stompCommand.getUuid(), stompCommand);
        }
    }
    public synchronized StompCommand pollFirstCommand() {
        if (toGoEmpty) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        StompCommand command = null;
        if (!toGoList.isEmpty()) {
            command = toGoList.poll();
        }
        return command;
    }
}
