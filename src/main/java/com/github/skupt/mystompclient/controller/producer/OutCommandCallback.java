package com.github.skupt.mystompclient.controller.producer;

import com.github.skupt.mystompclient.commands.StompCommand;

public interface OutCommandCallback {
    void onOutCommand(StompCommand command);
}
