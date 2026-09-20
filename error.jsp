<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BlackMart - Error</title>
    <link rel="stylesheet" href="../css/style.css">
</head>

<body>

<nav class="navbar">
    <div class="nav-brand">
        <a href="../../index.jsp">BlackMart</a>
    </div>

    <div class="nav-links">
        <a href="../../index.jsp">Home</a>
        <a href="../../pages/products.jsp">Products</a>
        <a href="../../pages/cart.jsp">Cart</a>
        <a href="../../pages/orders.jsp">Orders</a>
    </div>
</nav>

<main class="auth-page">

    <div class="auth-card">

        <h1>Something went wrong</h1>

        <p class="auth-subtitle">
            We could not complete your request.
        </p>

        <%
            String message = (String) request.getAttribute("errorMessage");

            if (message == null || message.trim().isEmpty()) {
                message = "Please try again later.";
            }
        %>

        <p class="form-message">
            <%= message %>
        </p>

        <a
            href="../../index.jsp"
            class="auth-button"
            style="display:block;text-align:center;margin-top:20px;">
            Back to Home
        </a>

    </div>

</main>

<footer class="footer">
    <p>&copy; 2026 BlackMart. All rights reserved.</p>
</footer>

</body>
</html>
