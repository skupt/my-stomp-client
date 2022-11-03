package com.github.skupt.mystompclient.zexamples;

import com.github.skupt.mystompclient.StompClient;
import com.github.skupt.mystompclient.commands.StompCommand;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PublisherTransactionExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        StompClient client = new StompClient("localhost", 61613);
        client.init();
        client.sendCommand(StompCommand.cmdConnect());
        TimeUnit.SECONDS.sleep(1);
        StompCommand begin = StompCommand.cmdBegin();
        String transactionId = begin.getUuid();
        client.sendCommand(begin);
        client.sendCommand(StompCommand.cmdSend("/stompTopic1/transactions", "Transaction msg #1", transactionId));
        client.sendCommand(StompCommand.cmdSend("/stompTopic1/transactions", "Transaction msg #2", transactionId));

        // RECEIPT frames issued with server on transaction SEND commands are coming back to client longer than duration
        // between 2 local calls: sendCommand(sendCmd) and sendCommand(commitCmd), so it is necessary to use conditional
        // wait like this or other convenient way to prevent Runtime Exception from local client about not all commands
        // in transaction are confirmed by server
        TimeUnit.MILLISECONDS.sleep(100);
        if (client.isAllTransactionCommandsReceipted(transactionId)) {
            client.sendCommand(StompCommand.cmdCommit(transactionId));
        } else {
            client.sendCommand(StompCommand.cmdAbort(transactionId));
        }

        client.sendCommand(StompCommand.cmdDisconnect());
        client.close();
    }
}
