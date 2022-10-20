package com.github.skupt.mystompclient.commands;

import java.util.HashMap;
import java.util.Map;

public class Connect extends StompCommand {
    String login;
    String passcode;

    public Connect() {
        super(Command.CONNECT, null, null);
    }

    public Connect(String login, String passcode) {
        super.command = Command.CONNECT;
        Map<String, String> headers = new HashMap<>();
        headers.put("login", login);
        headers.put("passcode", passcode);
        super.setHeaders(headers);
    }
}
