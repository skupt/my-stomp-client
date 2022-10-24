package com.github.skupt.mystompclient.controller.consumer;

import com.github.skupt.mystompclient.commands.StompCommand;

public interface CommandConsumerCallback {
    void onOutCommand(StompCommand command);

    void onInCommand(StompCommand command);
}
