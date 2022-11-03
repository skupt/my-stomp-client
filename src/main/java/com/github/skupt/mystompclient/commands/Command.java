package com.github.skupt.mystompclient.commands;

public enum Command {
    CONNECT, SEND, SUBSCRIBE, UNSUBSCRIBE, ACK, DISCONNECT,
    BEGIN, COMMIT, ABORT,
    CONNECTED, MESSAGE, RECEIPT, ERROR,
    STOP_MY_STOMP_PROCESSING
}
