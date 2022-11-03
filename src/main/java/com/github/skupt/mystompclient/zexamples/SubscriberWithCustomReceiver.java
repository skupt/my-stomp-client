package com.github.skupt.mystompclient.zexamples;

import com.github.skupt.mystompclient.StompClient;
import com.github.skupt.mystompclient.commands.StompCommand;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SubscriberWithCustomReceiver {
    public static void main(String[] args) throws IOException, InterruptedException {
        StompClient client = new StompClient("localhost", 61613, m -> System.out.println(m.getBody()));
        client.init();
        client.sendCommand(StompCommand.cmdConnect());
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdSubscribe("/stompTopic/one", false));
        TimeUnit.MINUTES.sleep(1);
        client.sendCommand(StompCommand.cmdDisconnect());
        client.close();
    }
}
