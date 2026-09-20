<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0">

    <title>Orders - BlackMart</title>

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

                <h1>My Orders</h1>

                <p>
                    View your previous BlackMart orders.
                </p>

            </div>

        </div>


        <div id="ordersContainer">

            <div class="loading">
                Loading orders...
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
        loadOrders
    );


    async function loadOrders() {

        const container =
            document.getElementById(
                "ordersContainer"
            );


        try {

            const response =
                await fetch(
                    "../api/orders?action=my-orders"
                );


            const result =
                await response.json();


            if (!response.ok) {

                container.innerHTML =
                    `<div class="error-message">
                        ${escapeHtml(
                            result.message ||
                            "Please login to view your orders."
                        )}
                    </div>`;

                return;
            }


            const orders =
                result.data || [];


            displayOrders(orders);

        } catch (error) {

            console.error(error);

            container.innerHTML =
                `<div class="error-message">
                    Unable to load orders.
                 </div>`;
        }
    }


    function displayOrders(orders) {

        const container =
            document.getElementById(
                "ordersContainer"
            );


        if (!orders || orders.length === 0) {

            container.innerHTML =
                `<div class="empty-message">

                    <h2>No orders yet</h2>

                    <p>
                        Your placed orders will appear here.
                    </p>

                    <br>

                    <a
                            href="products.jsp"
                            class="btn btn-primary">
                        Start Shopping
                    </a>

                </div>`;

            return;
        }


        let html =
            `<div class="orders-list">`;


        orders.forEach(order => {

            const total =
                Number(
                    order.totalAmount || 0
                );


            html += `

                <div class="order-card">

                    <div class="order-header">

                        <div>

                            <h3>
                                Order #${order.id}
                            </h3>

                            <p>
                                ${formatDate(
                                    order.createdAt
                                )}
                            </p>

                        </div>


                        <span class="status-badge">
                            ${escapeHtml(
                                order.status || "PLACED"
                            )}
                        </span>

                    </div>


                    <div class="order-details">

                        <div>

                            <strong>
                                Total
                            </strong>

                            <p>
                                ₹${total.toFixed(2)}
                            </p>

                        </div>


                        <div>

                            <strong>
                                Payment
                            </strong>

                            <p>
                                ${escapeHtml(
                                    order.paymentStatus ||
                                    "PAID"
                                )}
                            </p>

                        </div>


                        <div>

                            <strong>
                                Shipping Address
                            </strong>

                            <p>
                                ${escapeHtml(
                                    order.shippingAddress ||
                                    "Not available"
                                )}
                            </p>

                        </div>

                    </div>


                    <div class="order-actions">

                        <button
                                type="button"
                                class="btn btn-outline"
                                onclick="viewOrder(
                                    ${order.id}
                                )">

                            View Items

                        </button>

                    </div>


                    <div
                            id="items-${order.id}"
                            class="order-items">
                    </div>

                </div>

            `;
        });


        html += `</div>`;


        container.innerHTML = html;
    }


    async function viewOrder(orderId) {

        const itemsContainer =
            document.getElementById(
                `items-${orderId}`
            );


        if (
            itemsContainer.innerHTML.trim() !== ""
        ) {

            itemsContainer.innerHTML = "";

            return;
        }


        itemsContainer.innerHTML =
            `<p class="loading">
                Loading items...
             </p>`;


        try {

            const response =
                await fetch(
                    `../api/orders?action=items&orderId=${orderId}`
                );


            const result =
                await response.json();


            if (!response.ok) {

                itemsContainer.innerHTML =
                    `<p class="error-message">
                        ${escapeHtml(
                            result.message ||
                            "Unable to load items."
                        )}
                    </p>`;

                return;
            }


            const items =
                result.data || [];


            if (items.length === 0) {

                itemsContainer.innerHTML =
                    `<p>
                        No items found.
                     </p>`;

                return;
            }


            let html =
                `<h4>Order Items</h4>`;


            items.forEach(item => {

                const price =
                    Number(
                        item.price || 0
                    );

                const quantity =
                    Number(
                        item.quantity || 0
                    );


                html += `

                    <div class="order-item">

                        <span>
                            Product #${item.productId}
                        </span>

                        <span>
                            ${quantity} ×
                            ₹${price.toFixed(2)}
                        </span>

                    </div>

                `;
            });


            itemsContainer.innerHTML =
                html;


        } catch (error) {

            console.error(error);

            itemsContainer.innerHTML =
                `<p class="error-message">
                    Unable to load order items.
                 </p>`;
        }
    }


    function formatDate(dateValue) {

        if (!dateValue) {
            return "Date unavailable";
        }

        const date =
            new Date(dateValue);

        if (Number.isNaN(date.getTime())) {
            return String(dateValue);
        }

        return date.toLocaleString();
    }


    async function logout() {

        try {

            await fetch(
                "../api/auth?action=logout",
                {
                    method: "POST"
                }
            );

        } catch (error) {

            console.error(error);

        } finally {

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
