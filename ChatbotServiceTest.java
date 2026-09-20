package com.blackmart.blackmart.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatbotServiceTest {

    private final ChatbotService chatbotService =
            new ChatbotService();

    @Test
    void shouldReplyToGreeting() {
        String response =
                chatbotService.getResponse("hello");

        assertNotNull(response);
        assertFalse(response.trim().isEmpty());
    }

    @Test
    void shouldReplyToProductQuestion() {
        String response =
                chatbotService.getResponse("products");

        assertNotNull(response);
        assertFalse(response.trim().isEmpty());
    }

    @Test
    void shouldReplyToCartQuestion() {
        String response =
                chatbotService.getResponse("cart");

        assertNotNull(response);
        assertFalse(response.trim().isEmpty());
    }

    @Test
    void shouldReplyToOrderQuestion() {
        String response =
                chatbotService.getResponse("order");

        assertNotNull(response);
        assertFalse(response.trim().isEmpty());
    }

    @Test
    void shouldReplyToHelpQuestion() {
        String response =
                chatbotService.getResponse("help");

        assertNotNull(response);
        assertFalse(response.trim().isEmpty());
    }
}
