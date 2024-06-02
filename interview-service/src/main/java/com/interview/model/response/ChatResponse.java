package com.interview.model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.List;

@Getter
@Setter
public class ChatResponse {
    private Result result;

    @Getter
    @Setter
    public static class Result {
        private List<Alternative> alternatives;
        private Usage usage;
        private String modelVersion;

        @Getter
        @Setter
        public static class Alternative {
            private Message message;
            private String status;

            @Getter
            @Setter
            public static class Message {
                private String role;
                private String text;
            }
        }

        @Getter
        @Setter
        public static class Usage {
            private int inputTextTokens;
            private int completionTokens;
            private int totalTokens;
        }
    }

    public static String extractAssistantMessage(ChatResponse chatResponse) throws JsonProcessingException {
        StringBuilder assistantMessages = new StringBuilder();

        if (chatResponse.getResult() != null && chatResponse.getResult().getAlternatives() != null) {
            for (Result.Alternative alternative : chatResponse.getResult().getAlternatives()) {
                Result.Alternative.Message message = alternative.getMessage();
                if ("assistant".equals(message.getRole())) {
                    assistantMessages.append(message.getText());
                    assistantMessages.append("\n"); // Добавляем перенос строки между сообщениями
                }
            }
        }
        return assistantMessages.toString();
    }
}


