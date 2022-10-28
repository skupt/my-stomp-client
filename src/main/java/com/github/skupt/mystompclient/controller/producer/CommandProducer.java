package com.github.skupt.mystompclient.controller.producer;

import com.github.skupt.mystompclient.commands.StompCommand;
import com.github.skupt.mystompclient.service.ReceiveQueService;
import com.github.skupt.mystompclient.service.SentQueueService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

//@NoArgsConstructor
//@Data
public class CommandProducer implements FrameCallback, OutCommandCallback {
    public static Level loggerLevel = Level.INFO;
    private static Logger logger = Logger.getLogger(FrameCallback.class.getName());

    static {
        logger.setLevel(loggerLevel);
    }

    private String host;
    private int port;
    private Socket socket;
    private ServerListener serverListener;
    private BufferedReader bufferedReader;
    private PrintStream printStream;
    private SentQueueService sentQueueService; //
    private ReceiveQueService receiveQueService;
    private FrameParser frameParser = new FrameParser();

    public CommandProducer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connectHost() throws IOException {
        socket = new Socket(host, port);
        printStream = new PrintStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        serverListener = new ServerListener(bufferedReader, frameCallback());
        serverListener.start();
    }

    public void setQueues(SentQueueService sentQueueService, ReceiveQueService receiveQueService) {
        this.sentQueueService = sentQueueService;
        this.receiveQueService = receiveQueService;
    }

    @Override
    public void onFrame(String frame) {
        StompCommand command = frameParser.parseStompCommand(frame);
        if (command == null) {
            throw new RuntimeException("Command = null from frame: " + frame);
        }
        receiveQueService.addLastCommand(command);
    }

    @Override
    public void onOutCommand(StompCommand command) {
        sentQueueService.addLastOutCommand(command);

    }

    public FrameCallback frameCallback() {
        return this;
    }

    public void disconnectHost() throws IOException {
        try {
            logger.info("Command producer stopping server listening");
            TimeUnit.MILLISECONDS.sleep(20);
            serverListener.stopListening();
            bufferedReader.close();
            socket.close();
            printStream.close();
            logger.info("Disconnected from Server");
        } catch (StringIndexOutOfBoundsException e) {
            //do nothing
        } catch (RuntimeException e) {
            System.out.println("Exceptions");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public void setFrameParser(FrameParser frameParser) {
        this.frameParser = frameParser;
    }
}
