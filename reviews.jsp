<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BlackMart - Reviews</title>
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
        <div class="dashboard-header">
            <div>
                <h1>Product Reviews</h1>
                <p>View and submit customer reviews.</p>
            </div>
        </div>

        <div id="reviewSummary" class="dashboard-panel">
            <h2>Rating Summary</h2>
            <p id="averageRating">Loading...</p>
        </div>

        <div class="dashboard-panel">
            <div class="panel-header">
                <h2>Write a Review</h2>
            </div>

            <form id="reviewForm">
                <div class="form-group">
                    <label for="rating">Rating</label>
                    <select id="rating" required>
                        <option value="">Select rating</option>
                        <option value="5">★★★★★ - 5</option>
                        <option value="4">★★★★ - 4</option>
                        <option value="3">★★★ - 3</option>
                        <option value="2">★★ - 2</option>
                        <option value="1">★ - 1</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="comment">Comment</label>
                    <textarea
                        id="comment"
                        rows="5"
                        placeholder="Write your review..."
                        required></textarea>
                </div>

                <button type="submit" class="auth-button">
                    Submit Review
                </button>

                <p id="reviewMessage" class="form-message"></p>
            </form>
        </div>

        <div class="dashboard-panel">
            <div class="panel-header">
                <h2>Customer Reviews</h2>
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
    const productId = params.get("productId");

    const reviewsList = document.getElementById("reviewsList");
    const averageRating = document.getElementById("averageRating");
    const reviewForm = document.getElementById("reviewForm");
    const reviewMessage = document.getElementById("reviewMessage");

    function escapeHtml(value) {
        const div = document.createElement("div");
        div.textContent = value ?? "";
        return div.innerHTML;
    }

    function loadReviews() {
        if (!productId) {
            reviewsList.innerHTML = "<p>Product ID is missing.</p>";
            averageRating.textContent = "No product selected.";
            return;
        }

        fetch("../api/reviews?productId=" + encodeURIComponent(productId))
            .then(response => response.json())
            .then(data => {

                if (!data.success) {
                    reviewsList.innerHTML =
                        "<p>" + escapeHtml(data.message || "Unable to load reviews.") + "</p>";
                    return;
                }

                const reviews = data.data || [];

                if (reviews.length === 0) {
                    reviewsList.innerHTML = "<p>No reviews yet.</p>";
                    averageRating.textContent = "No ratings yet.";
                    return;
                }

                let total = 0;

                reviews.forEach(review => {
                    total += Number(review.rating);
                });

                const average = total / reviews.length;

                averageRating.textContent =
                    "Average Rating: " + average.toFixed(1) +
                    " / 5 (" + reviews.length + " reviews)";

                reviewsList.innerHTML = reviews.map(review => `
                    <div class="order-card">
                        <div class="order-header">
                            <strong>${escapeHtml(review.userName || "Customer")}</strong>
                            <span class="status-badge">
                                ${"★".repeat(Number(review.rating))}
                            </span>
                        </div>

                        <div class="order-details">
                            <p>${escapeHtml(review.comment || "")}</p>
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

    reviewForm.addEventListener("submit", function(event) {
        event.preventDefault();

        if (!productId) {
            reviewMessage.textContent = "Product ID is missing.";
            return;
        }

        const rating = document.getElementById("rating").value;
        const comment = document.getElementById("comment").value.trim();

        if (!rating || !comment) {
            reviewMessage.textContent =
                "Please enter a rating and comment.";
            return;
        }

        fetch("../api/reviews?action=create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                productId: Number(productId),
                rating: Number(rating),
                comment: comment
            })
        })
        .then(response => response.json())
        .then(data => {

            reviewMessage.textContent =
                data.message || "Review submitted.";

            if (data.success) {
                reviewForm.reset();
                loadReviews();
            }
        })
        .catch(error => {
            console.error(error);
            reviewMessage.textContent =
                "Unable to submit review.";
        });
    });

    loadReviews();
</script>

</body>
</html>
