package com.github.skupt.mystompclient.commands;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class StompCommand {
    public static final String STOMP_END_MSG_CHAR = "" + Character.MIN_VALUE;

    private final String uuid = UUID.randomUUID().toString();

    protected boolean sent = false;

    protected boolean receipted = false;
    protected Command command;
    protected Map<String, String> headers;
    protected String body;

    public StompCommand() {
    }

    public StompCommand(Command command, Map<String, String> headers, String body) {
        this.command = command;
        this.headers = headers;
        this.body = body;
    }

    public String toString() {
        String res = command.toString() + "\n"
                + formatHeaders() + "\n"
                + (body == null ? "" : body) + STOMP_END_MSG_CHAR;
        return res;
    }

    private String formatHeaders() {
        if (headers == null) return "";
        String headerBlock = "";
        for (Map.Entry<String, String> header : headers.entrySet()) {
            headerBlock += header.getKey() + ":" + header.getValue() + "\n";
        }

        return headerBlock;
    }

    public static StompCommand cmdConnect() {
        StompCommand connect = new StompCommand(Command.CONNECT, null, null);
        return connect;
    }

    public static StompCommand cmdConnect(String login, String passcode) {
        StompCommand connect = new StompCommand(Command.CONNECT, null, null);
        Map<String, String> headers = new HashMap<>();
        headers.put("login", login);
        headers.put("passcode", passcode);
        connect.setHeaders(headers);
        return connect;
    }

    public static StompCommand cmdDisconnect() {
        return new StompCommand(Command.DISCONNECT, null, null);
    }

    public static StompCommand cmdSend(String destination, String body) {
        StompCommand send = new StompCommand();
        send.setCommand(Command.SEND);
        send.setHeaders(new HashMap<>());
        send.getHeaders().put("destination", destination);
        send.setBody(body);
        return send;
    }

    public static StompCommand cmdSend(String destination, String body, boolean toReceipt) {
        StompCommand send = new StompCommand();
        send.setCommand(Command.SEND);
        send.setHeaders(new HashMap<>());
        send.getHeaders().put("destination", destination);
        send.setBody(body);
        if (toReceipt) send.getHeaders().put("receipt", send.getUuid());
        return send;
    }

    public static StompCommand cmdSubscribe(String destination, boolean ack) {
        StompCommand subscribe = new StompCommand();
        subscribe.setCommand(Command.SUBSCRIBE);
        subscribe.setHeaders(new HashMap<>());
        subscribe.getHeaders().put("destination", destination);
        subscribe.getHeaders().put("ack", ack ? "client" : "auto");
        return subscribe;
    }


}
