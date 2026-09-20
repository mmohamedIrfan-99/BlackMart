const API_BASE = "api";


document.addEventListener("DOMContentLoaded", () => {
    loadProducts();
});


async function loadProducts() {

    const productGrid = document.getElementById("productGrid");

    if (!productGrid) {
        return;
    }

    const searchInput = document.getElementById("searchInput");
    const categoryFilter = document.getElementById("categoryFilter");

    const search = searchInput
        ? searchInput.value.trim()
        : "";

    const category = categoryFilter
        ? categoryFilter.value
        : "";

    productGrid.innerHTML =
        '<div class="loading">Loading products...</div>';

    try {

        let url =
            `${API_BASE}/products?search=${encodeURIComponent(search)}&category=${encodeURIComponent(category)}`;

        const response = await fetch(url);

        const result = await response.json();

        if (!response.ok) {
            throw new Error(
                result.message || "Unable to load products"
            );
        }

        const products = result.data || [];

        displayProducts(products);

    } catch (error) {

        console.error(error);

        productGrid.innerHTML =
            `<div class="error-message">
                Unable to load products.
             </div>`;
    }
}


function displayProducts(products) {

    const productGrid =
        document.getElementById("productGrid");

    if (!productGrid) {
        return;
    }

    if (!products || products.length === 0) {

        productGrid.innerHTML =
            `<div class="empty-message">
                No products found.
             </div>`;

        return;
    }

    productGrid.innerHTML = "";

    products.forEach(product => {

        const card =
            document.createElement("div");

        card.className = "product-card";

        const image =
            product.imageUrl
                ? `<img src="${escapeHtml(product.imageUrl)}"
                        alt="${escapeHtml(product.name)}">`
                : `<div class="no-image">
                        No Image
                   </div>`;

        card.innerHTML = `

            <div class="product-image">
                ${image}
            </div>

            <div class="product-info">

                <h3>
                    ${escapeHtml(product.name)}
                </h3>

                <p class="product-description">
                    ${escapeHtml(
                        product.description || "No description available"
                    )}
                </p>

                ${
                    product.category
                        ? `<span class="product-category">
                            ${escapeHtml(product.category)}
                           </span>`
                        : ""
                }

                <div class="product-bottom">

                    <div>

                        <div class="product-price">
                            ₹${formatPrice(product.price)}
                        </div>

                        <div class="product-stock">
                            ${
                                product.stock > 0
                                    ? `${product.stock} available`
                                    : "Out of stock"
                            }
                        </div>

                    </div>

                    <button
                        class="btn btn-primary"
                        onclick="addToCart(${product.id})"
                        ${product.stock <= 0 ? "disabled" : ""}>
                        Add
                    </button>

                </div>

            </div>
        `;

        productGrid.appendChild(card);
    });
}


async function addToCart(productId) {

    try {

        const response = await fetch(
            `${API_BASE}/cart?action=add`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    productId: productId,
                    quantity: 1
                })
            }
        );

        const result = await response.json();

        if (!response.ok) {

            alert(
                result.message ||
                "Please login before adding products to cart."
            );

            return;
        }

        alert("Product added to cart!");

    } catch (error) {

        console.error(error);

        alert(
            "Unable to add product to cart."
        );
    }
}


/* =========================
   CHATBOT
   ========================= */

function toggleChatbot() {

    const chatbot =
        document.getElementById("chatbotBox");

    if (!chatbot) {
        return;
    }

    chatbot.classList.toggle("active");
}


function handleChatKey(event) {

    if (event.key === "Enter") {
        sendChatMessage();
    }
}


async function sendChatMessage() {

    const input =
        document.getElementById("chatInput");

    const messages =
        document.getElementById("chatMessages");

    if (!input || !messages) {
        return;
    }

    const message =
        input.value.trim();

    if (!message) {
        return;
    }

    addChatMessage(
        message,
        "user"
    );

    input.value = "";

    try {

        const response = await fetch(
            `${API_BASE}/chat`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    message: message
                })
            }
        );

        const result = await response.json();

        if (!response.ok) {

            addChatMessage(
                "Sorry, something went wrong.",
                "bot"
            );

            return;
        }

        const reply =
            result.data &&
            result.data.reply
                ? result.data.reply
                : "Sorry, I couldn't process your message.";

        addChatMessage(
            reply,
            "bot"
        );

    } catch (error) {

        console.error(error);

        addChatMessage(
            "Unable to connect to BlackMart Assistant.",
            "bot"
        );
    }
}


function addChatMessage(message, type) {

    const messages =
        document.getElementById("chatMessages");

    if (!messages) {
        return;
    }

    const div =
        document.createElement("div");

    div.className =
        type === "user"
            ? "user-message"
            : "bot-message";

    div.textContent = message;

    messages.appendChild(div);

    messages.scrollTop =
        messages.scrollHeight;
}


/* =========================
   HELPERS
   ========================= */

function formatPrice(price) {

    const number =
        Number(price);

    if (Number.isNaN(number)) {
        return "0.00";
    }

    return number.toFixed(2);
}


function escapeHtml(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
          }
