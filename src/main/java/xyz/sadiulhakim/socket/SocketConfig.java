package xyz.sadiulhakim.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
@EnableScheduling
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stock").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Use a shared scheduled executor
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadFactory(Thread.ofVirtual().factory());
        scheduler.initialize();

        registry.enableSimpleBroker("/topic", "/queue")
                .setTaskScheduler(scheduler);

        registry.setApplicationDestinationPrefixes("/stock");
        registry.setUserDestinationPrefix("/user");
    }
}
