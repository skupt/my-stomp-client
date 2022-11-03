package com.github.skupt.mystompclient.zexamples;

import com.github.skupt.mystompclient.StompClient;
import com.github.skupt.mystompclient.commands.StompCommand;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PublisherExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        StompClient client = new StompClient("localhost", 61613);
        client.init();
        client.sendCommand(StompCommand.cmdConnect("admin", "admin"));
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdSend("/stompTopic/one", "hello from send 1"));
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdSend("/stompTopic/two", "hello from send 2", true));
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdSend("/stompTopic/one", "hello from send 3"));
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdDisconnect());
        client.close();
    }
}
