package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.StompCommand;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MyStompApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        MyStompClient client = new MyStompClient("localhost", 61613);
        client.connectHost();
//        client.sendCommand(StompCommand.cmdConnect("admin", "admin"));
//        TimeUnit.SECONDS.sleep(3);
//        client.sendCommand(StompCommand.cmdDisconnect());
        client.disconnectHost();
        TimeUnit.MICROSECONDS.sleep(50);
    }
}
