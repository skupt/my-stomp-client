package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.StompCommand;
import com.github.skupt.mystompclient.controller.consumer.CommandConsumer;
import com.github.skupt.mystompclient.controller.consumer.InCommandPoller;
import com.github.skupt.mystompclient.controller.consumer.OutCommandPoller;
import com.github.skupt.mystompclient.controller.producer.CommandProducer;
import com.github.skupt.mystompclient.service.ReceiveQueService;
import com.github.skupt.mystompclient.service.SentQueueService;

import java.io.IOException;

public class StompClient {

    private String host;
    private int port;
    private ThreadGroup threadGroup;
    private ReceiveQueService receiveQueService;
    private SentQueueService sentQueueService;
    private CommandProducer commandProducer;
    private CommandConsumer commandConsumer;
    private Thread inCommandPoller;
    private Thread outCommandPoller;
    private StompReceiver stompReceiver;

    public StompClient(String host, int port, StompReceiver stompReceiver) {
        this.host = host;
        this.port = port;
        this.stompReceiver = stompReceiver;
    }

    public void init() throws IOException {
        threadGroup = new ThreadGroup("stomp");
        receiveQueService = new ReceiveQueService();
        sentQueueService = new SentQueueService();
        commandProducer = new CommandProducer(host, port);
        commandProducer.connectHost();
        commandConsumer = new CommandConsumer(sentQueueService, commandConsumer.getPrintStream());
        commandConsumer.setStompReceiver(stompReceiver);
        inCommandPoller = new Thread(threadGroup, new InCommandPoller(receiveQueService, commandConsumer.commandConsumerCallback()), "InCommandPoller");
        outCommandPoller = new Thread(threadGroup, new OutCommandPoller(sentQueueService, commandConsumer.commandConsumerCallback()), "OutCommandPoller");
        inCommandPoller.start();
        outCommandPoller.start();
    }

    public void sendCommand(StompCommand command) {
        commandProducer.onOutCommand(command);
    }
}
