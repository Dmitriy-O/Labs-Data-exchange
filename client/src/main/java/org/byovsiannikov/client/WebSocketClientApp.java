package org.byovsiannikov.client;

import com.google.gson.Gson;
import org.byovsiannikov.client.model.ChatMessage;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class WebSocketClientApp {

    private static final String URL = "ws://localhost:8081/ws"; // Порт сервера RTC
    private StompSession stompSession;
    private Gson gson = new Gson();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        WebSocketClientApp clientApp = new WebSocketClientApp();
        clientApp.connect();
        clientApp.startChat();
    }

    public void connect() {
        WebSocketStompClient stompClient = new WebSocketStompClient (new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        ListenableFuture<StompSession> future = stompClient.connect(URL, sessionHandler);
        try {
            stompSession = future.get();
            System.out.println("Підключено до WebSocket серверу RTC.");
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Не вдалося підключитися до WebSocket серверу RTC: " + e.getMessage());
        }
    }

    public void startChat() {
        if (stompSession == null || !stompSession.isConnected()) {
            System.out.println("Не вдалося встановити з'єднання з RTC сервером.");
            return;
        }

        // Підписка на топік
        stompSession.subscribe("/topic/public", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                ChatMessage message = (ChatMessage) payload;
                if (message.getType() == ChatMessage.MessageType.JOIN) {
                    System.out.println(message.getSender() + " приєднався до чату.");
                } else if (message.getType() == ChatMessage.MessageType.LEAVE) {
                    System.out.println(message.getSender() + " покинув чат.");
                } else {
                    System.out.println(message.getSender() + ": " + message.getContent());
                }
            }
        });

        // Введення імені користувача
        System.out.print("Введіть ваше ім'я: ");
        String username = scanner.nextLine();

        // Надсилання повідомлення про приєднання
        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setType(ChatMessage.MessageType.JOIN);
        joinMessage.setSender(username);
        stompSession.send("/app/chat.addUser", joinMessage);

        // Цикл для надсилання повідомлень
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("/exit")) {
                // Надсилання повідомлення про вихід
                ChatMessage leaveMessage = new ChatMessage();
                leaveMessage.setType(ChatMessage.MessageType.LEAVE);
                leaveMessage.setSender(username);
                stompSession.send("/app/chat.addUser", leaveMessage);
                try {
                    stompSession.disconnect();
                } catch (Exception e) {
                    System.out.println("Помилка при розриві з'єднання: " + e.getMessage());
                }
                System.out.println("Ви вийшли з чату.");
                break;
            } else {
                // Надсилання повідомлення
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setType(ChatMessage.MessageType.CHAT);
                chatMessage.setSender(username);
                chatMessage.setContent(input);
                stompSession.send("/app/chat.sendMessage", chatMessage);
            }
        }
    }

    private class MyStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("Підключення встановлено: " + connectedHeaders);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            System.out.println("Помилка транспорту RTC: " + exception.getMessage());
        }
    }
}
