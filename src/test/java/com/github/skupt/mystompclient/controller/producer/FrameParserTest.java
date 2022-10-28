package com.github.skupt.mystompclient.controller.producer;

import com.github.skupt.mystompclient.commands.StompCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FrameParserTest {
    private static String CONNECTED = "CONNECTED\n" +
            "server:ActiveMQ/5.17.1\n" +
            "heart-beat:0,0\n" +
            "session:ID:EPUAKYIW183B-61731-1666619636056-3:61\n" +
            "version:1.0\n" +
            "\n" +
            StompCommand.STOMP_END_MSG_CHAR;
    private static String MESSAGE = "MESSAGE\n" +
            "expires:0\n" +
            "destination:/queue//stompTopic/one\n" +
            "priority:4\n" +
            "message-id:ID:EPUAKYIW183B-61731-1666619636056-3:62:-1:1:1\n" +
            "timestamp:1666637661769\n" +
            "\n" +
            "hello from send 1" + StompCommand.STOMP_END_MSG_CHAR;
    private static String MESSAGE_START_N = "\nMESSAGE\n" +
            "expires:0\n" +
            "destination:/queue//stompTopic/one\n" +
            "priority:4\n" +
            "message-id:ID:EPUAKYIW183B-61731-1666619636056-3:62:-1:1:1\n" +
            "timestamp:1666637661769\n" +
            "\n" +
            "hello from send 1" + StompCommand.STOMP_END_MSG_CHAR;


    private FrameParser frameParser;

    @BeforeEach
    void init() {
        frameParser = new FrameParser();
    }

    @Test
    public void shouldReturnMessageCommandPart() {
        CommandParts commandParts = frameParser.parseCommand(MESSAGE);
        Assertions.assertEquals("MESSAGE", frameParser.parseCommand(MESSAGE).getCommand());
        Assertions.assertEquals("CONNECTED", frameParser.parseCommand(CONNECTED).getCommand());
        Assertions.assertEquals("MESSAGE", frameParser.parseCommand(MESSAGE_START_N).getCommand());

    }

}
