<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0">

    <title>Products - BlackMart</title>

    <link
            rel="stylesheet"
            href="../css/style.css">

</head>

<body>

<header class="navbar">

    <div class="logo">
        BlackMart
    </div>

    <nav>
        <a href="../index.jsp">Home</a>
        <a href="products.jsp">Products</a>
        <a href="cart.jsp">Cart</a>
        <a href="orders.jsp">Orders</a>
    </nav>

    <div class="nav-actions">

        <a
                href="login.jsp"
                class="btn btn-outline">
            Login
        </a>

        <a
                href="register.jsp"
                class="btn btn-primary">
            Register
        </a>

    </div>

</header>


<main>

    <section class="products-section">

        <div class="section-header">

            <div>

                <h1>All Products</h1>

                <p>
                    Browse products available on BlackMart.
                </p>

            </div>

        </div>


        <div class="filter-bar">

            <input
                    type="text"
                    id="searchInput"
                    placeholder="Search by product name...">


            <select id="categoryFilter">

                <option value="">
                    All Categories
                </option>

                <option value="Electronics">
                    Electronics
                </option>

                <option value="Fashion">
                    Fashion
                </option>

                <option value="Shoes">
                    Shoes
                </option>

                <option value="Accessories">
                    Accessories
                </option>

                <option value="Home">
                    Home
                </option>

            </select>


            <button
                    type="button"
                    class="btn btn-primary"
                    onclick="loadProducts()">

                Search

            </button>

        </div>


        <div
                id="productGrid"
                class="product-grid">

            <div class="loading">
                Loading products...
            </div>

        </div>

    </section>

</main>


<footer class="footer">

    <p>
        © 2026 BlackMart. All rights reserved.
    </p>

</footer>


<script src="../js/app.js"></script>

</body>
</html>
