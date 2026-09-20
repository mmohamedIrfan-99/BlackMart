package com.blackmart.blackmart.listener;

import com.blackmart.blackmart.util.DBUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("BlackMart: Initializing database...");

        try {
            InputStream inputStream =
                    event.getServletContext()
                            .getResourceAsStream("/WEB-INF/schema.sql");

            if (inputStream == null) {
                throw new RuntimeException("schema.sql not found!");
            }

            StringBuilder sql = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            inputStream,
                            StandardCharsets.UTF_8))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    line = line.trim();

                    if (!line.isEmpty() && !line.startsWith("--")) {
                        sql.append(line).append("\n");
                    }
                }
            }

            String[] statements = sql.toString().split(";");

            try (Connection connection =
                         DBUtil.getDataSource().getConnection();
                 Statement statement =
                         connection.createStatement()) {

                for (String query : statements) {

                    query = query.trim();

                    if (!query.isEmpty()) {
                        statement.execute(query);
                    }
                }
            }

            System.out.println(
                    "BlackMart: Database initialized successfully!"
            );

        } catch (Exception e) {

            System.err.println(
                    "BlackMart: Database initialization failed!"
            );

            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

        System.out.println("BlackMart: Database shutdown.");

        DBUtil.close();
    }
              }
