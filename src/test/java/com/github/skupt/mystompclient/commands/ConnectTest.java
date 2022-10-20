package com.github.skupt.mystompclient.commands;

import org.junit.jupiter.api.Test;

import static com.github.skupt.mystompclient.commands.StompCommand.STOMP_END_MSG_CHAR;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectTest {
    @Test
    public void shouldFormatConnectFrame() {
        Connect connect = new Connect();
        String expected = "CONNECT\n\n" + STOMP_END_MSG_CHAR;
        assertEquals(expected, connect.toString());
    }

    @Test
    public void shouldFormatConnectFrameLoginPassword() {
        Connect connect = new Connect("login", "pass");
        String expected = "CONNECT\nlogin:login\npasscode:pass\n\n" + STOMP_END_MSG_CHAR;
        assertEquals(expected, connect.toString());
    }
}
