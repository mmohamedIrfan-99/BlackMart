package com.blackmart.blackmart.controller;

import com.blackmart.blackmart.model.User;
import com.blackmart.blackmart.service.AuthService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/auth")
public class AuthServlet extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            if ("register".equalsIgnoreCase(action)) {
                register(request, response);
            } else if ("login".equalsIgnoreCase(action)) {
                login(request, response);
            } else if ("logout".equalsIgnoreCase(action)) {
                logout(request, response);
            } else {
                sendError(response, "Invalid authentication action");
            }
        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Something went wrong");
        }
    }

    private void register(HttpServletRequest request,
                          HttpServletResponse response)
            throws Exception {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        User user = authService.register(
                name,
                email,
                password,
                role
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Registration successful");
        result.put("user", safeUser(user));

        response.getWriter().write(gson.toJson(result));
    }

    private void login(HttpServletRequest request,
                       HttpServletResponse response)
            throws Exception {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = authService.login(email, password);

        HttpSession oldSession = request.getSession(false);

        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);

        request.changeSessionId();

        session.setAttribute("user", user);
        session.setMaxInactiveInterval(30 * 60);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Login successful");
        result.put("user", safeUser(user));

        response.getWriter().write(gson.toJson(result));
    }

    private void logout(HttpServletRequest request,
                        HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Logout successful");

        response.getWriter().write(gson.toJson(result));
    }

    private Map<String, Object> safeUser(User user) {

        Map<String, Object> data = new HashMap<>();

        data.put("id", user.getId());
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());

        return data;
    }

    private void sendError(HttpServletResponse response,
                           String message)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put(
                "message",
                message == null ? "Request failed" : message
        );

        response.getWriter().write(gson.toJson(result));
    }
          }
