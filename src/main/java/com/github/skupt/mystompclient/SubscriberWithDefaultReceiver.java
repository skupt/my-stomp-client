package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.StompCommand;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SubscriberWithDefaultReceiver {
    public static void main(String[] args) throws IOException, InterruptedException {
        StompClient client = new StompClient("localhost", 61613);
        client.init();
        client.sendCommand(StompCommand.cmdConnect());
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdSubscribe("/stompTopic/one", false));
        TimeUnit.SECONDS.sleep(15);
        client.sendCommand(StompCommand.cmdUnsubscribe("/stompTopic/one"));
        TimeUnit.SECONDS.sleep(15);
        client.sendCommand(StompCommand.cmdDisconnect());
        client.close();
    }
}
