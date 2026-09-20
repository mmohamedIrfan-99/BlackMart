<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>BlackMart</title>

    <link rel="stylesheet" href="css/style.css">
</head>

<body>

<header class="navbar">

    <div class="logo">
        BlackMart
    </div>

    <nav>
        <a href="index.jsp">Home</a>
        <a href="pages/products.jsp">Products</a>
        <a href="pages/cart.jsp">Cart</a>
        <a href="pages/orders.jsp">Orders</a>
    </nav>

    <div class="nav-actions">
        <a href="pages/login.jsp" class="btn btn-outline">Login</a>
        <a href="pages/register.jsp" class="btn btn-primary">Register</a>
    </div>

</header>


<main>

    <section class="hero">

        <div class="hero-content">

            <h1>Welcome to BlackMart</h1>

            <p>
                Your smart multi-seller marketplace for shopping,
                selling and discovering amazing products.
            </p>

            <div class="hero-buttons">
                <a href="pages/products.jsp" class="btn btn-primary">
                    Shop Now
                </a>

                <a href="pages/register.jsp" class="btn btn-outline">
                    Become a Seller
                </a>
            </div>

        </div>

    </section>


    <section class="products-section">

        <div class="section-header">

            <div>
                <h2>Featured Products</h2>

                <p>
                    Explore products from BlackMart sellers.
                </p>
            </div>

            <a href="pages/products.jsp" class="view-all">
                View All
            </a>

        </div>


        <div class="filter-bar">

            <input
                    type="text"
                    id="searchInput"
                    placeholder="Search products..."
            >

            <select id="categoryFilter">

                <option value="">All Categories</option>
                <option value="Electronics">Electronics</option>
                <option value="Fashion">Fashion</option>
                <option value="Shoes">Shoes</option>
                <option value="Accessories">Accessories</option>
                <option value="Home">Home</option>

            </select>

            <button
                    type="button"
                    class="btn btn-primary"
                    onclick="loadProducts()">
                Search
            </button>

        </div>


        <div id="productGrid" class="product-grid">

            <div class="loading">
                Loading products...
            </div>

        </div>

    </section>


    <section class="features">

        <div class="feature-card">

            <div class="feature-icon">
                🛒
            </div>

            <h3>Easy Shopping</h3>

            <p>
                Browse products, add them to your cart
                and checkout easily.
            </p>

        </div>


        <div class="feature-card">

            <div class="feature-icon">
                🏪
            </div>

            <h3>Multi-Seller</h3>

            <p>
                Sellers can create and manage their
                own product listings.
            </p>

        </div>


        <div class="feature-card">

            <div class="feature-icon">
                💳
            </div>

            <h3>Simple Checkout</h3>

            <p>
                Complete your orders using our
                project mock payment system.
            </p>

        </div>


        <div class="feature-card">

            <div class="feature-icon">
                ⭐
            </div>

            <h3>Reviews & Ratings</h3>

            <p>
                Share your shopping experience through
                product reviews and ratings.
            </p>

        </div>

    </section>

</main>


<!-- Chatbot -->

<button
        id="chatbotButton"
        class="chatbot-button"
        onclick="toggleChatbot()">

    💬

</button>


<div id="chatbotBox" class="chatbot-box">

    <div class="chatbot-header">

        <strong>BlackMart Assistant</strong>

        <button
                type="button"
                onclick="toggleChatbot()">
            ×
        </button>

    </div>


    <div id="chatMessages" class="chat-messages">

        <div class="bot-message">
            Hi! 👋 How can I help you with BlackMart?
        </div>

    </div>


    <div class="chat-input">

        <input
                type="text"
                id="chatInput"
                placeholder="Ask something..."
                onkeydown="handleChatKey(event)"
        >

        <button
                type="button"
                onclick="sendChatMessage()">
            Send
        </button>

    </div>

</div>


<footer class="footer">

    <p>
        © 2026 BlackMart. All rights reserved.
    </p>

</footer>


<script src="js/app.js"></script>

</body>
</html>
