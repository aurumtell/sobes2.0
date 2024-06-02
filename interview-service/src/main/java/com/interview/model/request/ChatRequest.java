package com.interview.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatRequest {
    private String modelUri;
    private List<Message> messages;
    private CompletionOptions completionOptions;

    @Getter
    @Setter
    // Inner class representing completion options
    public static class CompletionOptions {
        private boolean stream;
        private double temperature;
        private int maxTokens;

        public CompletionOptions(boolean stream, double temperature, int maxTokens) {
            this.stream = stream;
            this.temperature = temperature;
            this.maxTokens = maxTokens;
        }
    }

    @Getter
    @Setter
    // Inner class representing a message
    public static class Message {
        private String role;
        private String text;

        public Message(String role, String text) {
            this.role = role;
            this.text = text;
        }

    }

    // Constructor
    public ChatRequest(String modelUri, List<Message> messages, CompletionOptions completionOptions) {
        this.modelUri = modelUri;
        this.messages = messages;
        this.completionOptions = completionOptions;
    }

    public static ChatRequest createWithUserAndSystemMessages(String modelUri, String userMessage, String system, CompletionOptions completionOptions) {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("user", userMessage));
        messages.add(new Message("system", system));
        return new ChatRequest(modelUri, messages, completionOptions); // Passing null for completion options
    }

}




