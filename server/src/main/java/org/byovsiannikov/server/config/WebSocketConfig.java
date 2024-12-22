package org.byovsiannikov.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Префікс для брокера
        config.setApplicationDestinationPrefixes("/app"); // Префікс для відправки повідомлень
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Спробуйте без SockJS
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }

}
