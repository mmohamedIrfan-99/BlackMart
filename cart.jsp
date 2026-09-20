<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0">

    <title>Cart - BlackMart</title>

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

        <button
                type="button"
                class="btn btn-primary"
                onclick="logout()">
            Logout
        </button>

    </div>

</header>


<main>

    <section class="products-section">

        <div class="section-header">

            <div>

                <h1>Shopping Cart</h1>

                <p>
                    Review your products before checkout.
                </p>

            </div>

        </div>


        <div id="cartContainer">

            <div class="loading">
                Loading cart...
            </div>

        </div>

    </section>

</main>


<footer class="footer">

    <p>
        © 2026 BlackMart. All rights reserved.
    </p>

</footer>


<script>

    document.addEventListener(
        "DOMContentLoaded",
        loadCart
    );


    async function loadCart() {

        const container =
            document.getElementById("cartContainer");

        try {

            const response =
                await fetch("../api/cart");

            const result =
                await response.json();


            if (!response.ok) {

                container.innerHTML =
                    `<div class="error-message">
                        ${escapeHtml(
                            result.message ||
                            "Please login to view your cart."
                        )}
                    </div>`;

                return;
            }


            const cart =
                result.data || [];

            displayCart(cart);

        } catch (error) {

            console.error(error);

            container.innerHTML =
                `<div class="error-message">
                    Unable to load cart.
                 </div>`;
        }
    }


    function displayCart(cart) {

        const container =
            document.getElementById("cartContainer");


        if (!cart || cart.length === 0) {

            container.innerHTML =
                `<div class="empty-message">

                    <h2>Your cart is empty</h2>

                    <p>
                        Add some products to your cart.
                    </p>

                    <br>

                    <a
                            href="products.jsp"
                            class="btn btn-primary">
                        Continue Shopping
                    </a>

                </div>`;

            return;
        }


        let total = 0;


        let html = `

            <div class="cart-list">
        `;


        cart.forEach(item => {

            const price =
                Number(item.price || 0);

            const quantity =
                Number(item.quantity || 0);

            const subtotal =
                price * quantity;

            total += subtotal;


            html += `

                <div class="cart-item">

                    <div class="cart-item-info">

                        <h3>
                            ${escapeHtml(
                                item.productName ||
                                "Product"
                            )}
                        </h3>

                        <p>
                            ₹${price.toFixed(2)}
                            × ${quantity}
                        </p>

                    </div>


                    <div class="cart-item-actions">

                        <button
                                type="button"
                                onclick="updateQuantity(
                                    ${item.productId},
                                    ${quantity - 1}
                                )">
                            −
                        </button>


                        <span>
                            ${quantity}
                        </span>


                        <button
                                type="button"
                                onclick="updateQuantity(
                                    ${item.productId},
                                    ${quantity + 1}
                                )">
                            +
                        </button>


                        <strong>
                            ₹${subtotal.toFixed(2)}
                        </strong>


                        <button
                                type="button"
                                class="remove-btn"
                                onclick="removeItem(
                                    ${item.productId}
                                )">
                            Remove
                        </button>

                    </div>

                </div>

            `;
        });


        html += `

            </div>


            <div class="cart-summary">

                <h2>
                    Cart Total:
                    ₹${total.toFixed(2)}
                </h2>

                <div class="cart-summary-actions">

                    <a
                            href="products.jsp"
                            class="btn btn-outline">
                        Continue Shopping
                    </a>

                    <a
                            href="checkout.jsp"
                            class="btn btn-primary">
                        Proceed to Checkout
                    </a>

                </div>

            </div>

        `;


        container.innerHTML = html;
    }


    async function updateQuantity(
        productId,
        quantity
    ) {

        if (quantity <= 0) {

            await removeItem(productId);

            return;
        }


        try {

            const response =
                await fetch(
                    "../api/cart?action=update",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            productId: productId,
                            quantity: quantity
                        })
                    }
                );


            const result =
                await response.json();


            if (!response.ok) {

                alert(
                    result.message ||
                    "Unable to update cart."
                );

                return;
            }


            loadCart();

        } catch (error) {

            console.error(error);

            alert(
                "Unable to update cart."
            );
        }
    }


    async function removeItem(productId) {

        try {

            const response =
                await fetch(
                    "../api/cart?action=remove",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            productId: productId
                        })
                    }
                );


            const result =
                await response.json();


            if (!response.ok) {

                alert(
                    result.message ||
                    "Unable to remove item."
                );

                return;
            }


            loadCart();

        } catch (error) {

            console.error(error);

            alert(
                "Unable to remove item."
            );
        }
    }


    async function logout() {

        try {

            await fetch(
                "../api/auth?action=logout",
                {
                    method: "POST"
                }
            );

            window.location.href =
                "../index.jsp";

        } catch (error) {

            console.error(error);

            window.location.href =
                "../index.jsp";
        }
    }


    function escapeHtml(value) {

        if (
            value === null ||
            value === undefined
        ) {
            return "";
        }

        return String(value)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

</script>

</body>
</html>
