package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.StompCommand;
import com.github.skupt.mystompclient.controller.consumer.CommandConsumer;
import com.github.skupt.mystompclient.controller.consumer.InCommandPoller;
import com.github.skupt.mystompclient.controller.consumer.OutCommandPoller;
import com.github.skupt.mystompclient.controller.producer.CommandProducer;
import com.github.skupt.mystompclient.receiver.DefaultStompReceiver;
import com.github.skupt.mystompclient.receiver.StompReceiver;
import com.github.skupt.mystompclient.service.ReceiveQueService;
import com.github.skupt.mystompclient.service.SentQueueService;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * StompClient class initializes all underlying messaging components and is a context holder for them.
 */
public class StompClient {
    public static Level loggerLevel = Level.INFO;

    private String host;
    private int port;
    private ThreadGroup threadGroup;
    @Getter
    private ReceiveQueService receiveQueService;
    @Getter
    private SentQueueService sentQueueService;
    private CommandProducer commandProducer;
    private CommandConsumer commandConsumer;
    private InCommandPoller inPollerRunnable;
    private OutCommandPoller outPollerRunnable;

    private Thread inCommandPoller;
    private Thread outCommandPoller;
    private StompReceiver stompReceiver;

    private DefaultStompReceiver defaultStompReceiver;

    public StompClient(String host, int port, StompReceiver stompReceiver) {
        this.host = host;
        this.port = port;
        this.stompReceiver = stompReceiver;
    }

    public StompClient(String host, int port) {
        this.host = host;
        this.port = port;
        defaultStompReceiver = new DefaultStompReceiver();
    }

    public void init() throws IOException {
        threadGroup = new ThreadGroup("stomp");
        receiveQueService = new ReceiveQueService();
        receiveQueService.setStompClient(this);
        sentQueueService = new SentQueueService();
        sentQueueService.setStompClient(this);
        commandProducer = new CommandProducer(host, port);
        commandProducer.setQueues(sentQueueService, receiveQueService);
        commandProducer.connectHost();
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        commandConsumer = new CommandConsumer(sentQueueService, commandProducer.getPrintStream());
        if (stompReceiver == null) commandConsumer.addStompReceiver(defaultStompReceiver);
        if (stompReceiver != null) commandConsumer.addStompReceiver(stompReceiver);
        inPollerRunnable = new InCommandPoller(receiveQueService, commandConsumer.commandConsumerCallback());
        inCommandPoller = new Thread(threadGroup, inPollerRunnable, "InCommandPoller");
        outPollerRunnable = new OutCommandPoller(sentQueueService, commandConsumer.commandConsumerCallback());
        outCommandPoller = new Thread(threadGroup, outPollerRunnable, "OutCommandPoller");
        inCommandPoller.start();
        outCommandPoller.start();
    }

    public void sendCommand(StompCommand command) {
        commandProducer.onOutCommand(command);
    }

    public void close() throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        inPollerRunnable.stop = true;
        outPollerRunnable.stop = true;
        commandProducer.disconnectHost();
        threadGroup.stop();
    }

    public boolean isAllTransactionCommandsReceipted(String transactionId) {
        return sentQueueService.isAllTransactionCommandsReceipted(transactionId);
    }
}
