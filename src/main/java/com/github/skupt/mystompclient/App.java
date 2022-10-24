package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.Connect;
import com.github.skupt.mystompclient.commands.StompCommand;

public class App {
    public static void main(String[] args) {
        StompReceiver stompReceiver = System.out::println;
        StompClient client = new StompClient("localhost", 61613, stompReceiver);
        StompCommand stompCommand = new Connect("admin", "admin");
        client.sendCommand(stompCommand);
    }
}
