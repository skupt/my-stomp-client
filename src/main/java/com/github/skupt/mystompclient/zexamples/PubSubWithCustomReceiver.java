package com.github.skupt.mystompclient.zexamples;

import com.github.skupt.mystompclient.StompClient;
import com.github.skupt.mystompclient.commands.StompCommand;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PubSubWithCustomReceiver {
    public static void main(String[] args) throws IOException, InterruptedException {
        StompClient client = new StompClient("localhost", 61613, m ->
                System.out.println("Body of message from server: " + m.getBody()));
        client.init();
        client.sendCommand(StompCommand.cmdConnect());
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdSubscribe("/stompTopic/one", false));
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdSend("/stompTopic/one", "Pub/Sub message 1."));
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdSend("/stompTopic/one", "Pub/Sub message 2."));
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdSend("/stompTopic/one", "Pub/Sub message 3."));
        TimeUnit.SECONDS.sleep(1);
        client.sendCommand(StompCommand.cmdDisconnect());
        client.close();
    }
}
