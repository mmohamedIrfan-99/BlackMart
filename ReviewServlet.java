package com.blackmart.blackmart.controller;

import com.blackmart.blackmart.model.Review;
import com.blackmart.blackmart.model.User;
import com.blackmart.blackmart.service.ReviewService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/reviews")
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        setJson(response);

        try {
            long productId = parseId(
                    request.getParameter("productId")
            );

            List<Review> reviews =
                    reviewService.getProductReviews(productId);

            double average =
                    reviewService.getAverageRating(productId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("reviews", reviews);
            result.put("averageRating", average);

            response.getWriter().write(
                    gson.toJson(result)
            );

        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Unable to load reviews");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        setJson(response);

        try {
            User user = getLoggedUser(request);

            if (!"BUYER".equalsIgnoreCase(user.getRole())) {
                sendForbidden(response);
                return;
            }

            long productId = parseId(
                    request.getParameter("productId")
            );

            int rating = parseRating(
                    request.getParameter("rating")
            );

            String comment =
                    request.getParameter("comment");

            reviewService.addReview(
                    user.getId(),
                    productId,
                    rating,
                    comment
            );

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put(
                    "message",
                    "Review added successfully"
            );

            response.getWriter().write(
                    gson.toJson(result)
            );

        } catch (IllegalArgumentException e) {
            sendError(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Unable to add review");
        }
    }

    private User getLoggedUser(HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            throw new IllegalArgumentException(
                    "Login required"
            );
        }

        User user =
                (User) session.getAttribute("user");

        if (user == null) {
            throw new IllegalArgumentException(
                    "Login required"
            );
        }

        return user;
    }

    private long parseId(String value) {

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Product ID is required"
            );
        }

        try {
            long id = Long.parseLong(value);

            if (id <= 0) {
                throw new IllegalArgumentException(
                        "Invalid product ID"
                );
            }

            return id;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid product ID"
            );
        }
    }

    private int parseRating(String value) {

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Rating is required"
            );
        }

        try {
            int rating = Integer.parseInt(value);

            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException(
                        "Rating must be between 1 and 5"
                );
            }

            return rating;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid rating"
            );
        }
    }

    private void setJson(HttpServletResponse response) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    private void sendError(HttpServletResponse response,
                           String message)
            throws IOException {

        response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put(
                "message",
                message == null
                        ? "Request failed"
                        : message
        );

        response.getWriter().write(
                gson.toJson(result)
        );
    }

    private void sendForbidden(HttpServletResponse response)
            throws IOException {

        response.setStatus(
                HttpServletResponse.SC_FORBIDDEN
        );

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "Buyer access required");

        response.getWriter().write(
                gson.toJson(result)
        );
    }
}
