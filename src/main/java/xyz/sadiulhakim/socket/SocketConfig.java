package xyz.sadiulhakim.socket;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${trading.socket.endpoint:''}")
    private String socketEndpoint;

    @Value("${trading.application.destination.prefix:''}")
    private String appPrefix;

    @Value("${trading.user.destination.prefix:''}")
    private String userPrefix;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(socketEndpoint).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Use a shared scheduled executor
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadFactory(Thread.ofVirtual().factory());
        scheduler.initialize();

        registry.enableSimpleBroker("/topic", "/queue")
                .setTaskScheduler(scheduler);

        registry.setApplicationDestinationPrefixes(appPrefix);
        registry.setUserDestinationPrefix(userPrefix);
    }
}
