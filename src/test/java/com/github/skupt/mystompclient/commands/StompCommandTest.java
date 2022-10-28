package com.github.skupt.mystompclient.commands;

import org.junit.jupiter.api.Test;

import static com.github.skupt.mystompclient.commands.StompCommand.STOMP_END_MSG_CHAR;

public class StompCommandTest {

    @Test
    public void shouldParseDisconnected() {
        String frame = "CONNECT\n\n" + STOMP_END_MSG_CHAR;
        StompCommand disconnected;
    }
}
