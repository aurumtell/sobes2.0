package com.chat.controller;

import com.chat.model.request.Message;
import com.chat.model.response.MessageResponse;
import com.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired
    MessageService messageService;
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public MessageResponse sendMessage(Message message) {
        System.out.println("Received message: " + message.getText());
        return messageService.sendMessage(message);
    }
}
