package com.github.skupt.mystompclient.commands;

import com.github.skupt.mystompclient.StompFrameParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConnectedTest {

    @Test
    public void shouldParseConnectedWithSessionHeader() {
        String frame = "CONNECTED\n" +
                "session: <session-id>\n" +
                "\n" +
                StompCommand.STOMP_END_MSG_CHAR;

        StompCommand connected = StompFrameParser.parseStompCommand(frame);
        Assertions.assertEquals("CONNECTED", connected.getCommand().toString());
        Assertions.assertEquals(1, connected.getHeaders().size());
        Assertions.assertNull(connected.getBody());
    }
}
