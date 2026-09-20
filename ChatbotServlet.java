package com.blackmart.blackmart.controller;

import com.blackmart.blackmart.dto.ApiResponse;
import com.blackmart.blackmart.dto.ChatRequest;
import com.blackmart.blackmart.dto.ChatResponse;
import com.blackmart.blackmart.service.ChatbotService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/chat")
public class ChatbotServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final ChatbotService chatbotService = new ChatbotService();

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            StringBuilder body = new StringBuilder();

            try (BufferedReader reader = request.getReader()) {
                String line;

                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            }

            ChatRequest chatRequest =
                    gson.fromJson(body.toString(), ChatRequest.class);

            if (chatRequest == null ||
                    chatRequest.getMessage() == null ||
                    chatRequest.getMessage().trim().isEmpty()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                response.getWriter().write(
                        gson.toJson(
                                ApiResponse.error("Message is required")
                        )
                );

                return;
            }

            String reply =
                    chatbotService.getReply(chatRequest.getMessage());

            ChatResponse chatResponse =
                    new ChatResponse(reply);

            response.getWriter().write(
                    gson.toJson(
                            ApiResponse.success(
                                    "Chat response generated",
                                    chatResponse
                            )
                    )
            );

        } catch (Exception e) {

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().write(
                    gson.toJson(
                            ApiResponse.error(
                                    "Unable to process chat request"
                            )
                    )
            );
        }
    }
}
