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

    public static StompCommand cmdSend(String destination, String body, String transactionId) {
        StompCommand send = new StompCommand();
        send.setCommand(Command.SEND);
        send.setHeaders(new HashMap<>());
        send.getHeaders().put("destination", destination);
        send.getHeaders().put("receipt", send.getUuid());
        send.getHeaders().put("transaction", transactionId);
        send.setBody(body);
        return send;
    }

    /**
     * @param destination String topic name
     * @param ack         boolean 'client' acknowledgment if true (When a client has issued a SUBSCRIBE frame with the
     *                    ack header set to client any messages received from that destination will not be considered to
     *                    have been consumed (by the server) until the message has been acknowledged via an ACK frame
     *                    sent from client) or 'auto'
     */
    public static StompCommand cmdSubscribe(String destination, boolean ack) {
        StompCommand subscribe = new StompCommand();
        subscribe.setCommand(Command.SUBSCRIBE);
        subscribe.setHeaders(new HashMap<>());
        subscribe.getHeaders().put("destination", destination);
        subscribe.getHeaders().put("ack", ack ? "client" : "auto");
        return subscribe;
    }

    public static StompCommand cmdUnsubscribe(String destination) {
        StompCommand unsubscribe = new StompCommand();
        unsubscribe.setCommand(Command.UNSUBSCRIBE);
        unsubscribe.setHeaders(new HashMap<>());
        unsubscribe.getHeaders().put("destination", destination);
        return unsubscribe;
    }

    public static StompCommand cmdAck(String messageId) {
        StompCommand ack = new StompCommand();
        ack.setCommand(Command.ACK);
        ack.setHeaders(new HashMap<>());
        ack.getHeaders().put("ack", messageId);
        return ack;
    }

    /**
     * BEGIN is a command for start transaction. Each following SEND frame must contain header 'transaction' with
     * its id. Identifier is set automatically,  you can get it from this command with method .getUuid()
     */
    public static StompCommand cmdBegin() {
        StompCommand begin = new StompCommand();
        begin.setCommand(Command.BEGIN);
        begin.setHeaders(new HashMap<>());
        begin.getHeaders().put("transaction", begin.uuid);
        return begin;
    }

    public static StompCommand cmdCommit(String transactionId) {
        StompCommand commit = new StompCommand();
        commit.setCommand(Command.COMMIT);
        commit.setHeaders(new HashMap<>());
        commit.getHeaders().put("transaction", transactionId);
        return commit;
    }

    public static StompCommand cmdAbort(String transactionId) {
        StompCommand abort = new StompCommand();
        abort.setCommand(Command.ABORT);
        abort.setHeaders(new HashMap<>());
        abort.getHeaders().put("transaction", transactionId);
        return abort;
    }


}
