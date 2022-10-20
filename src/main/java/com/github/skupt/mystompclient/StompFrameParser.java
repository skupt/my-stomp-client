package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.Command;
import com.github.skupt.mystompclient.commands.StompCommand;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StompFrameParser {
    private static String headerRegex = "(?<key>[^\\n:]*):(?<value>[^\\n]*)\\n";
    private static Pattern headerPattern = Pattern.compile(headerRegex, Pattern.UNICODE_CHARACTER_CLASS);

    public static void main(String[] args) {
        String frame = "CONNECTED\r\n" +
                "session1: <session-id>\n" +
                "session2: <session-id2>\n" +
                "\n"
                + "body1\n"
                + "body2\n"
                + "body3"
                + StompCommand.STOMP_END_MSG_CHAR;
        CommandParts cp = parseCommand(frame);
//        System.out.println(cp.command);
//        System.out.println();
//        System.out.println(cp.headers);
//        System.out.println();
//        System.out.println(cp.body);

        String headerPart = cp.getHeaders();
        System.out.println(headerPart);
        System.out.println();
//        Map<String, String> headers = parseHeaders(headerPart);
//        headers.entrySet().forEach(System.out::println);


        StompCommand stompCommand = parseStompCommand(frame);
        System.out.println(stompCommand);


    }

    public static StompCommand parseStompCommand(String frame) {
        CommandParts commandParts = parseCommand(frame);
        Command command = Command.valueOf(commandParts.getCommand());
        Map<String, String> headers = parseHeaders(commandParts.getHeaders());

        return new StompCommand(command, headers, commandParts.getBody());
    }

    private static CommandParts parseCommand(String frame) {
        frame = removeFirstR(frame);
        String command = frame.substring(0, frame.indexOf("\n"));
        String rest = frame.substring(frame.indexOf("\n") + 1);
        String headers = rest.substring(0, rest.indexOf("\n\n"));
        rest = rest.substring(rest.indexOf("\n\n") + 2);
        String body = rest.substring(0, rest.indexOf(Character.MIN_VALUE));
        CommandParts commandParts = new CommandParts();
        commandParts.setCommand(command);
        commandParts.setHeaders(headers);
        commandParts.setBody(body.length() == 0 ? null : body);
        return commandParts;
    }

    private static String removeFirstR(String frame) {
        int rindex1 = frame.indexOf('\r');
        int nindex1 = frame.indexOf('\n');
        if ((rindex1 != -1 & nindex1 != -1) & (rindex1 < nindex1)) {
            String s1 = frame.substring(0, rindex1);
            String s2 = frame.substring(rindex1 + 1);
            frame = s1.concat(s2);
        }
        return frame;
    }


    @Data
    private static class CommandParts {
        String command;
        String headers;
        String body;
    }

    private static Map<String, String> parseHeaders(String headersPart) {
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


//    private static Map<String, String> parseHeaders1(String headersPart) {
//        Map<String, String> headerMap = new HashMap<>();
//        if (headersPart == null) return headerMap;
//        Matcher headerMatcher = headerPattern.matcher(headersPart);
//        while (headerMatcher.matches()) {
//            String key = headerMatcher.group("key");
//            String value = headerMatcher.group("value");
//            System.out.println(key + ":" + value);
//            headerMap.put(key, value);
//        }
//        return headerMap;
//    }


}
