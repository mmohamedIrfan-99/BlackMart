<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BlackMart - Product Details</title>
    <link rel="stylesheet" href="../css/style.css">
</head>

<body>

<nav class="navbar">
    <div class="nav-brand">
        <a href="../index.jsp">BlackMart</a>
    </div>

    <div class="nav-links">
        <a href="../index.jsp">Home</a>
        <a href="products.jsp">Products</a>
        <a href="cart.jsp">Cart</a>
        <a href="orders.jsp">Orders</a>
        <a href="login.jsp">Login</a>
    </div>
</nav>

<main class="dashboard-page">

    <section class="dashboard-section">

        <div id="productDetails" class="dashboard-panel">
            <p>Loading product...</p>
        </div>

        <div class="dashboard-panel">
            <div class="panel-header">
                <h2>Customer Reviews</h2>
                <a id="reviewLink" href="reviews.jsp">
                    View Reviews
                </a>
            </div>

            <div id="reviewsList">
                Loading reviews...
            </div>
        </div>

    </section>

</main>

<footer class="footer">
    <p>&copy; 2026 BlackMart. All rights reserved.</p>
</footer>

<script>
    const params = new URLSearchParams(window.location.search);
    const productId = params.get("id");

    const productDetails = document.getElementById("productDetails");
    const reviewsList = document.getElementById("reviewsList");
    const reviewLink = document.getElementById("reviewLink");

    function escapeHtml(value) {
        const div = document.createElement("div");
        div.textContent = value ?? "";
        return div.innerHTML;
    }

    function formatPrice(value) {
        return "₹" + Number(value || 0).toFixed(2);
    }

    function loadProduct() {
        if (!productId) {
            productDetails.innerHTML =
                "<p>Product ID is missing.</p>";
            return;
        }

        fetch("../api/products?id=" + encodeURIComponent(productId))
            .then(response => response.json())
            .then(data => {

                if (!data.success) {
                    productDetails.innerHTML =
                        "<p>" +
                        escapeHtml(data.message || "Product not found.") +
                        "</p>";
                    return;
                }

                const product = data.data;

                productDetails.innerHTML = `
                    <div class="product-detail">
                        <div class="product-detail-image">
                            <img
                                src="${escapeHtml(product.imageUrl || "https://via.placeholder.com/400")}"
                                alt="${escapeHtml(product.name)}"
                            >
                        </div>

                        <div class="product-detail-info">
                            <h1>${escapeHtml(product.name)}</h1>

                            <p class="product-price">
                                ${formatPrice(product.price)}
                            </p>

                            <p>
                                ${escapeHtml(product.description || "No description available.")}
                            </p>

                            <p>
                                <strong>Category:</strong>
                                ${escapeHtml(product.category || "General")}
                            </p>

                            <p>
                                <strong>Available Stock:</strong>
                                ${escapeHtml(product.stock)}
                            </p>

                            <div class="form-group">
                                <label for="quantity">Quantity</label>
                                <input
                                    type="number"
                                    id="quantity"
                                    value="1"
                                    min="1"
                                    max="${escapeHtml(product.stock)}"
                                >
                            </div>

                            <button
                                class="auth-button"
                                onclick="addToCart(${product.id})">
                                Add to Cart
                            </button>

                            <p id="cartMessage" class="form-message"></p>
                        </div>
                    </div>
                `;

                reviewLink.href =
                    "reviews.jsp?productId=" +
                    encodeURIComponent(product.id);

                loadReviews(product.id);
            })
            .catch(error => {
                console.error(error);
                productDetails.innerHTML =
                    "<p>Unable to load product.</p>";
            });
    }

    function addToCart(id) {
        const quantity =
            Number(document.getElementById("quantity").value);

        const cartMessage =
            document.getElementById("cartMessage");

        if (quantity < 1) {
            cartMessage.textContent =
                "Quantity must be at least 1.";
            return;
        }

        fetch("../api/cart?action=add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                productId: id,
                quantity: quantity
            })
        })
        .then(response => response.json())
        .then(data => {
            cartMessage.textContent =
                data.message ||
                (data.success
                    ? "Product added to cart."
                    : "Unable to add product.");
        })
        .catch(error => {
            console.error(error);
            cartMessage.textContent =
                "Unable to add product to cart.";
        });
    }

    function loadReviews(id) {
        fetch("../api/reviews?productId=" + encodeURIComponent(id))
            .then(response => response.json())
            .then(data => {

                if (!data.success) {
                    reviewsList.innerHTML =
                        "<p>Unable to load reviews.</p>";
                    return;
                }

                const reviews = data.data || [];

                if (reviews.length === 0) {
                    reviewsList.innerHTML =
                        "<p>No reviews yet.</p>";
                    return;
                }

                reviewsList.innerHTML = reviews.map(review => `
                    <div class="order-card">
                        <div class="order-header">
                            <strong>
                                ${escapeHtml(review.userName || "Customer")}
                            </strong>

                            <span class="status-badge">
                                ${"★".repeat(Number(review.rating))}
                            </span>
                        </div>

                        <div class="order-details">
                            <p>
                                ${escapeHtml(review.comment || "")}
                            </p>

                            <small>
                                ${escapeHtml(review.createdAt || "")}
                            </small>
                        </div>
                    </div>
                `).join("");
            })
            .catch(error => {
                console.error(error);
                reviewsList.innerHTML =
                    "<p>Unable to load reviews.</p>";
            });
    }

    loadProduct();
</script>

</body>
</html>
