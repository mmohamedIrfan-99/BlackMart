const productParams = new URLSearchParams(window.location.search);
const productId = productParams.get("id");

const productContainer = document.getElementById("productDetails");
const reviewContainer = document.getElementById("reviewsList");

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
        productContainer.innerHTML =
            "<p>Product ID is missing.</p>";
        return;
    }

    fetch("../api/products?id=" + encodeURIComponent(productId))
        .then(response => response.json())
        .then(data => {
            if (!data.success) {
                productContainer.innerHTML =
                    "<p>" +
                    escapeHtml(
                        data.message || "Product not found."
                    ) +
                    "</p>";
                return;
            }

            const product = data.data;

            productContainer.innerHTML = `
                <div class="product-detail">
                    <div class="product-detail-image">
                        <img
                            src="${escapeHtml(
                                product.imageUrl ||
                                "https://via.placeholder.com/400"
                            )}"
                            alt="${escapeHtml(product.name)}"
                        >
                    </div>

                    <div class="product-detail-info">
                        <h1>${escapeHtml(product.name)}</h1>

                        <p class="product-price">
                            ${formatPrice(product.price)}
                        </p>

                        <p>
                            ${escapeHtml(
                                product.description ||
                                "No description available."
                            )}
                        </p>

                        <p>
                            <strong>Category:</strong>
                            ${escapeHtml(
                                product.category || "General"
                            )}
                        </p>

                        <p>
                            <strong>Stock:</strong>
                            ${escapeHtml(product.stock)}
                        </p>

                        <div class="form-group">
                            <label for="quantity">
                                Quantity
                            </label>

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
                            onclick="addProductToCart(${product.id})">
                            Add to Cart
                        </button>

                        <p
                            id="cartMessage"
                            class="form-message">
                        </p>
                    </div>
                </div>
            `;

            loadReviews(product.id);
        })
        .catch(error => {
            console.error(error);

            productContainer.innerHTML =
                "<p>Unable to load product.</p>";
        });
}

function addProductToCart(id) {
    const quantity =
        Number(document.getElementById("quantity").value);

    const message =
        document.getElementById("cartMessage");

    if (quantity < 1) {
        message.textContent =
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
            message.textContent =
                data.message ||
                (data.success
                    ? "Product added to cart."
                    : "Unable to add product.");
        })
        .catch(error => {
            console.error(error);
            message.textContent =
                "Unable to add product to cart.";
        });
}

function loadReviews(id) {
    fetch(
        "../api/reviews?productId=" +
        encodeURIComponent(id)
    )
        .then(response => response.json())
        .then(data => {
            if (!data.success) {
                reviewContainer.innerHTML =
                    "<p>Unable to load reviews.</p>";
                return;
            }

            const reviews = data.data || [];

            if (reviews.length === 0) {
                reviewContainer.innerHTML =
                    "<p>No reviews yet.</p>";
                return;
            }

            reviewContainer.innerHTML =
                reviews.map(review => `
                    <div class="order-card">
                        <div class="order-header">
                            <strong>
                                ${escapeHtml(
                                    review.userName ||
                                    "Customer"
                                )}
                            </strong>

                            <span class="status-badge">
                                ${"★".repeat(
                                    Number(review.rating)
                                )}
                            </span>
                        </div>

                        <div class="order-details">
                            <p>
                                ${escapeHtml(
                                    review.comment || ""
                                )}
                            </p>

                            <small>
                                ${escapeHtml(
                                    review.createdAt || ""
                                )}
                            </small>
                        </div>
                    </div>
                `).join("");
        })
        .catch(error => {
            console.error(error);
            reviewContainer.innerHTML =
                "<p>Unable to load reviews.</p>";
        });
}

loadProduct();
