package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.Command;
import com.github.skupt.mystompclient.commands.StompCommand;

import java.util.*;

public class SentQueueService {
    private Set<String> destinationToAck = new HashSet<>();
    private Map<String, StompCommand> messagesToGo = new LinkedHashMap<>();
    private Map<String, StompCommand> messagesToReceipt = new HashMap<>();
    private LinkedHashMap<String, StompCommand> inDestinations = new LinkedHashMap<>();

    public void queOutCommand(StompCommand stompCommand) {
        if (stompCommand.getCommand() == Command.SEND) {
            String destination = stompCommand.getHeaders().get("destination");
            // handle destination header
            if (destination != null) messagesToGo.put(stompCommand.getUuid(), stompCommand);
            else
                throw new RuntimeException("Command SEND must have not null destination header. Command uuid: " + stompCommand.getUuid());
            // handle receipt header
            String receipt = stompCommand.getHeaders().get("receipt");
            if (receipt != null) messagesToReceipt.put(stompCommand.getUuid(), stompCommand);
        }
    }
}
