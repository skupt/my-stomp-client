package com.github.skupt.mystompclient.service;

import com.github.skupt.mystompclient.commands.StompCommand;

import java.util.LinkedList;

public class ReceiveQueService {
    private LinkedList<StompCommand> receivedList = new LinkedList<>();

    public void addLastCommand(StompCommand command) {
        receivedList.add(command);
    }
}
