package com.github.skupt.mystompclient.controller.producer;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;

@Data
public class ServerListener extends Thread {
    private boolean run = true;

    private BufferedReader reader;

    private FrameCallback frameCallback;

    public ServerListener(BufferedReader bufferedReader, FrameCallback frameCallback) {
        this.reader = bufferedReader;
        this.frameCallback = frameCallback;
    }

    @Override
    public void run() {
        char c;
        while (run) {
            StringBuilder sb = new StringBuilder();
            try {


                do {
                    try {
                        c = (char) reader.read();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    sb.append(c);
                } while (c != Character.MIN_VALUE && run);
                frameCallback.onFrame(sb.toString());
            } catch (StringIndexOutOfBoundsException e) {
                // it is consequence of closing client
                return;
            }
        }
    }

    public void stopListening() {
        this.run = false;
    }
}
