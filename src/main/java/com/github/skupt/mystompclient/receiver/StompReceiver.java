package com.github.skupt.mystompclient.receiver;

import com.github.skupt.mystompclient.commands.StompCommand;

public interface StompReceiver {
    void onMessage(StompCommand stompCommand);
}
