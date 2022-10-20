package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.StompCommand;
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
    private static Logger logger = Logger.getLogger(FrameCallback.class.getName());

    static {
        logger.setLevel(Level.INFO);
    }

    private String host;
    private int port;
    private Socket socket;
    private ServerListener serverListener;
    private BufferedReader bufferedReader;
    private PrintStream printStream;

    private SentQueueService sentQueueService;

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
        logger.info("Connected to: " + host + ":" + port);
    }

    public void sendCommand(StompCommand command) {
        String cmdStr = command.toString();
        printStream.print(cmdStr);
        logger.info("Command sent: \n" + cmdStr);
    }

    @Override
    public void onFrame(String frame) {
        System.out.println(frame);
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
