package xyz.sadiulhakim.listener;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AppListener {

    @EventListener
    @Async("taskExecutor")
    void serverStarted(WebServerInitializedEvent event) {
        System.out.println("Application is running on port : " + event.getWebServer().getPort());
    }
}
