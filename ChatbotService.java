package com.blackmart.blackmart.service;

import com.blackmart.blackmart.dao.ProductDao;
import com.blackmart.blackmart.model.Product;

import java.util.List;

public class ChatbotService {

    private final ProductDao productDao;

    public ChatbotService() {
        this.productDao = new ProductDao();
    }

    public String getReply(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Hi! How can I help you with BlackMart?";
        }

        String text = message.toLowerCase().trim();

        if (text.contains("hello") || text.contains("hi") || text.contains("hey")) {
            return "Hello! Welcome to BlackMart. How can I help you?";
        }

        if (text.contains("cart")) {
            return "You can add products to your cart and manage the quantity from the Cart section.";
        }

        if (text.contains("order")) {
            return "You can check your previous orders from the Order History section.";
        }

        if (text.contains("payment")) {
            return "BlackMart supports mock payment during checkout for project demonstration.";
        }

        if (text.contains("return") || text.contains("refund")) {
            return "For return or refund-related questions, please contact the BlackMart administrator.";
        }

        if (text.contains("product") || text.contains("search")) {
            return "You can browse products and search by product name or category.";
        }

        if (text.contains("price") || text.contains("cost")) {
            return "Product prices are displayed on the product listing and product details.";
        }

        if (text.contains("seller")) {
            return "Sellers can add, update and manage their products from the Seller Dashboard.";
        }

        if (text.contains("admin")) {
            return "The administrator can manage users, products and orders from the Admin Dashboard.";
        }

        if (text.contains("help")) {
            return "I can help you with products, cart, orders, payments, sellers and general BlackMart information.";
        }

        return "Sorry, I didn't understand that. Try asking about products, cart, orders, payment or sellers.";
    }
}
