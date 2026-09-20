# BlackMart

BlackMart is a multi-seller e-commerce marketplace built using Java Servlets, H2 Database, JSP, HTML, CSS and JavaScript.

## Features

- User registration and login
- Buyer and Seller roles
- Seller product management
- Product search and category filtering
- Shopping cart
- Checkout with mock payment
- Buyer order history
- Seller incoming orders
- Order status management
- Product reviews and ratings
- Admin user management
- Admin product moderation
- Admin order management
- AI chatbot
- Responsive web interface

## Technology Stack

- Java 17
- Apache Tomcat 9
- Maven
- Java Servlets
- JSP
- HTML5
- CSS3
- JavaScript
- H2 Database
- HikariCP
- Gson
- jBCrypt
- JUnit 5
- Mockito
- SLF4J
- Logback
- GitHub Actions

## Architecture

```text
Browser
   |
   v
HTML / JSP / JavaScript
   |
   v
Servlet Controllers
   |
   v
Service Layer
   |
   v
DAO Layer
   |
   v
HikariCP
   |
   v
H2 Database
