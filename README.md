# Web Socket

## Configuration

1. configureMessageBroker
2. registerStompEndpoints

These two methods are part of Spring WebSockets with STOMP (Simple Text Oriented Messaging Protocol).
They help configure message handling between the client (browser) and server.

1️⃣ configureMessageBroker(MessageBrokerRegistry registry)
👉 What does it do?
It sets up the message broker (like a post office) that handles sending and receiving messages.

👉 Why do we need it?
Without a broker, clients cannot send or receive real-time messages.

👉 How does it work?

```java

@Override
public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/queue", "/topic");
    registry.setApplicationDestinationPrefixes(destinationPrefix);
}
```

Enables a built-in message broker for sending messages.
Messages with "/queue" and "/topic" will be handled by this broker.
/topic is for broadcasting messages to multiple users (like a news feed).
/queue is for sending messages to a specific user (like a private message).
registry.setApplicationDestinationPrefixes(destinationPrefix);

Defines a prefix (destinationPrefix) for application-level messages.
Clients will send messages to destinations starting with this prefix.
Example: If destinationPrefix = "/app", clients will send messages like:

```javascript
stompClient.send("/app/stockPrice", {}, JSON.stringify({symbol: "HK"}));
```

The server will receive this and process it.

2️⃣ registerStompEndpoints(StompEndpointRegistry registry)
👉 What does it do?
It sets up WebSocket endpoints (like a URL) where clients can connect.

👉 Why do we need it?
Without this, clients won’t know where to connect to WebSockets.

👉 How does it work?

```java
 @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .withSockJS();
    }
```

Adds an endpoint (e.g., /ws) for clients to connect.
withSockJS() enables SockJS, which helps when WebSockets are not supported.
Example:

If registerEndpoint = "/ws", the client will connect like this:

```javascript
var stompClient = Stomp.over(new SockJS('/ws'));
stompClient.connect({}, function () {
    console.log("Connected!");
});
```

This establishes a WebSocket connection to /ws.

### Summary

| Method | What it does | Why it's needed | How it works |
|--------|-------------|----------------|-------------|
| `configureMessageBroker` | Sets up message broker | Enables real-time messaging | Defines `/topic` and `/queue` for communication |
| `registerStompEndpoints` | Creates WebSocket endpoint | Allows clients to connect | Uses SockJS for compatibility |
