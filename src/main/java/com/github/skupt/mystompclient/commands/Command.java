package com.github.skupt.mystompclient.commands;

public enum Command {
    CONNECT, SEND, SUBSCRIBE, UNSUBSCRIBE, ACK, DISCONNECT,
    CONNECTED, MESSAGE, RECEIPT, ERROR,
    STOP_MY_STOMP_PROCESSING
}
