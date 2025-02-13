## SseEmitter in Spring Boot

### What is SseEmitter?
SseEmitter is a Spring MVC component used for Server-Sent Events (SSE), allowing the server to push real-time updates to clients over HTTP. It is useful for scenarios where real-time updates are required, but full-duplex communication is unnecessary.

### Example Usage
```java
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sse")
public class SseController {

    @GetMapping("/stream")
    public SseEmitter streamData() {
        SseEmitter emitter = new SseEmitter();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                emitter.send("Hello at " + System.currentTimeMillis());
            } catch (IOException e) {
                emitter.complete();
            }
        }, 0, 2, TimeUnit.SECONDS);
        return emitter;
    }
}
```

## WebSocket in Spring Boot

### What is WebSocket?
WebSocket is a full-duplex communication protocol that allows real-time communication between the client and server. It is useful for bidirectional data flow, such as chat applications, online games, and live notifications.

### Example Usage

#### WebSocket Configuration
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SimpleWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }
}
```

#### WebSocket Handler
```java
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SimpleWebSocketHandler extends TextWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("Received: " + message.getPayload()));
    }
}
```

## Difference between SseEmitter and WebSocket

| Feature          | SseEmitter                         | WebSocket                       |
|-----------------|----------------------------------|--------------------------------|
| Communication   | One-way (Server to Client)      | Two-way (Full Duplex)         |
| Protocol       | HTTP (Long-lived connection)    | WebSocket protocol (ws://)    |
| Performance    | Uses fewer resources            | More resource-intensive       |
| Scalability    | Better for high-scale systems   | Requires more connections     |
| Use Case       | Real-time updates, notifications | Chat apps, multiplayer games  |
| Connection Type | Multiple HTTP requests         | Persistent connection         |

SseEmitter is a better choice when you need server-to-client updates with lower resource consumption, while WebSocket is more suited for interactive applications requiring bidirectional communication.


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
It sets up WebSocket endpoints (like a URL) `where clients can connect`.

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
