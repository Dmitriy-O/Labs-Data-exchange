package org.byovsiannikov.server.controller;

import org.byovsiannikov.server.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/chat.sendMessage") // Коли клієнт надсилає повідомлення на /app/chat.sendMessage
    @SendTo("/topic/public") // Повідомлення буде відправлено на /topic/public
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

    @MessageMapping("/chat.addUser") // Коли клієнт надсилає повідомлення на /app/chat.addUser
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage message) {
        // Не изменяем тип сообщения, используем тот, который пришёл от клиента
        return message;
    }
}
