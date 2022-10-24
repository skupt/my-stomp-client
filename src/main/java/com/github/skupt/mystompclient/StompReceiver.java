package com.github.skupt.mystompclient;

import com.github.skupt.mystompclient.commands.StompCommand;

public interface StompReceiver {
    void onMessage(StompCommand stompCommand);
}
