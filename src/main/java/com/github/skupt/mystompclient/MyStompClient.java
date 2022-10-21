package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.StompCommand;
import com.github.skupt.mystompclient.service.ReceiveQueService;
import com.github.skupt.mystompclient.service.SentQueueService;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@NoArgsConstructor
@Data
public class MyStompClient implements FrameCallback {
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
    private SentQueueService sentQueueService;
    private ReceiveQueService receiveQueService;

    public MyStompClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connectHost() throws IOException {
        socket = new Socket(host, port);
        printStream = new PrintStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        serverListener = new ServerListener(bufferedReader, getFrameCallback());
        serverListener.start();
//        logger.info("Connected to: " + host + ":" + port);
    }

    public void queOutCommand(StompCommand command) {
        sentQueueService.addLastOutCommand(command);
    }

//    public void sendCommand(StompCommand command) {
//        String cmdStr = command.toString();
//        printStream.print(cmdStr);
//        logger.info("Command sent: \n" + cmdStr);
//    }

    @Override
    public void onFrame(String frame) {
        StompCommand command = StompFrameParser.parseStompCommand(frame);
        receiveQueService.addLastCommand(command);
    }

    public FrameCallback getFrameCallback() {
        return this;
    }

    public void disconnectHost() throws IOException {
        logger.info("Stopping server listening");
        serverListener.stopListening();
        bufferedReader.close();
        socket.close();
        printStream.close();
        logger.info("Disconnected from Server");

    }
}
