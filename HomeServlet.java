package com.blackmart.blackmart.controller;

import com.blackmart.blackmart.dao.ProductDao;
import com.blackmart.blackmart.model.Product;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/home")
public class HomeServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Product> products =
                    productDao.findActiveProducts(null, null);

            Map<String, Object> result = new HashMap<>();

            result.put("success", true);
            result.put("message", "Welcome to BlackMart");
            result.put("products", products);

            response.getWriter().write(
                    gson.toJson(result)
            );

        } catch (Exception e) {
            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            Map<String, Object> result = new HashMap<>();

            result.put("success", false);
            result.put(
                    "message",
                    "Unable to load BlackMart"
            );

            response.getWriter().write(
                    gson.toJson(result)
            );
        }
    }
}
