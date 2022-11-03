# About My Stomp client library

This is my implementation of client for STOMP protocol version 1.0
https://stomp.github.io/stomp-specification-1.0.html#frame-BEGIN.
Only text messages in ASCII or UTF-8 are supported on 2022-11-03. Messages with byte body will be added soon.
Transactions and all commands are supported.
Client is asynchronous.

# How to build library using maven

1. Download and unpack zip file with project.
2. Run maven commands from the root directory of project:

- `mvn clean`
- `mvn package`

3. Take `my-stomp-client-1.0-SNAPSHOT.jar` file from `target\` directory and put it to dependencies of your project.

# How to use it

1. Run some message broker that supports STOMP protocol (for example Apache ActiveMQ https://activemq.apache.org/)
2. Example of usage

```
public static void main(String[] args) throws IOException, InterruptedException {
StompClient stompClient = new StompClient("localhost", 61613);
stompClient.init();
stompClient.sendCommand(StompCommand.cmdConnect());
stompClient.sendCommand(StompCommand.cmdSubscribe("/stomp/topic222", false));
stompClient.sendCommand(StompCommand.cmdSend("/stomp/topic222", "Hello Stomp"));
stompClient.sendCommand(StompCommand.cmdDisconnect());
stompClient.close();
}
```

3. More examples you can find in `zexamples` package.
   There you can find how to use transactions, set your own command consumers in client instance.