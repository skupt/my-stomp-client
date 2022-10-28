package com.github.skupt.mystompclient.controller.producer;

import lombok.Data;

@Data
public class CommandParts {
    private String command;
    private String headers;
    private String body;
}
