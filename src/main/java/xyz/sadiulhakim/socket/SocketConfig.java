package xyz.sadiulhakim.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
@EnableScheduling
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final int POOL_SIZE = 10; // Choose an appropriate thread pool size
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(POOL_SIZE);

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stock").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Use a shared scheduled executor
        ConcurrentTaskScheduler taskScheduler = new ConcurrentTaskScheduler(scheduledExecutor);
        registry.enableSimpleBroker("/topic", "/queue").setTaskScheduler(taskScheduler);

        registry.setApplicationDestinationPrefixes("/stock");
        registry.setUserDestinationPrefix("/user");
    }
}
