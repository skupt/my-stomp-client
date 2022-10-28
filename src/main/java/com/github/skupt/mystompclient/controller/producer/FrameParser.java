package com.github.skupt.mystompclient.controller.producer;

import com.github.skupt.mystompclient.commands.Command;
import com.github.skupt.mystompclient.commands.StompCommand;

import java.util.HashMap;
import java.util.Map;

public class FrameParser {

    public StompCommand parseStompCommand(String frame) {
        CommandParts commandParts = parseCommand(frame);
        Command command = Command.valueOf(commandParts.getCommand());
        Map<String, String> headers = parseHeaders(commandParts.getHeaders());
        StompCommand stompCommand = new StompCommand(command, headers, commandParts.getBody());

        return stompCommand;
    }

    String removeFirstR(String frame) {
        String res = null;
        int rindex1 = frame.indexOf('\r');
        int nindex1 = frame.indexOf('\n');
        if ((rindex1 != -1 & nindex1 != -1) & (rindex1 < nindex1)) {
            String s1 = frame.substring(0, rindex1);
            String s2 = frame.substring(rindex1 + 1);
            res = s1 + s2;
        }
        return frame;
    }

    CommandParts parseCommand(String frame) {
        String frameRem = removeFirstR(frame);
        while (frameRem.charAt(0) == (char) 10 || frameRem.charAt(0) == (char) 13 || frameRem.charAt(0) == (char) 0)
            frameRem = frameRem.substring(1, frame.length());
        String command = frameRem.substring(0, frameRem.indexOf("\n"));
        String rest = frameRem.substring(frameRem.indexOf("\n") + 1);
        String headers = rest.substring(0, rest.indexOf("\n\n"));
        rest = rest.substring(rest.indexOf("\n\n") + 2);
        String body = rest.substring(0, rest.indexOf(Character.MIN_VALUE));
        CommandParts commandParts = new CommandParts();
        commandParts.setCommand(command);
        commandParts.setHeaders(headers);
        commandParts.setBody(body.length() == 0 ? null : body);

        return commandParts;
    }

    synchronized Map<String, String> parseHeaders(String headersPart) {
        Map<String, String> headerMap = new HashMap<>();
        if (headersPart == null) return headerMap;
        String[] header = headersPart.split("\\n");
        for (int i = 0; i < header.length; i++) {
            int splitIndex = header[i].indexOf(":");
            String key = header[i].substring(0, splitIndex);
            String value = header[i].substring(splitIndex + 1);
            headerMap.put(key, value);
        }
        return headerMap;
    }

}
