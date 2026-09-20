<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Admin Dashboard - BlackMart</title>

    <link rel="stylesheet"
          href="../css/style.css">

</head>

<body>

<header class="navbar">

    <div class="logo">
        BlackMart
    </div>

    <nav>
        <a href="../index.jsp">Home</a>
        <a href="admin.jsp">Admin Dashboard</a>
        <a href="products.jsp">Products</a>
    </nav>

    <div class="nav-actions">

        <button
                type="button"
                class="btn btn-primary"
                onclick="logout()">
            Logout
        </button>

    </div>

</header>


<main>

    <section class="dashboard-section">

        <div class="dashboard-header">

            <div>

                <h1>Admin Dashboard</h1>

                <p>
                    Manage BlackMart users, products and orders.
                </p>

            </div>

            <button
                    type="button"
                    class="btn btn-outline"
                    onclick="loadDashboard()">
                Refresh
            </button>

        </div>


        <div class="dashboard-stats">

            <div class="stat-card">

                <h3>Total Users</h3>

                <strong id="userCount">
                    0
                </strong>

            </div>


            <div class="stat-card">

                <h3>Total Products</h3>

                <strong id="productCount">
                    0
                </strong>

            </div>


            <div class="stat-card">

                <h3>Total Orders</h3>

                <strong id="orderCount">
                    0
                </strong>

            </div>

        </div>


        <!-- USERS -->

        <div class="dashboard-panel">

            <div class="panel-header">

                <h2>Users</h2>

                <button
                        type="button"
                        class="btn btn-outline"
                        onclick="loadUsers()">
                    Refresh
                </button>

            </div>


            <div id="usersContainer">

                <div class="loading">
                    Loading users...
                </div>

            </div>

        </div>


        <!-- PRODUCTS -->

        <div class="dashboard-panel">

            <div class="panel-header">

                <h2>Products</h2>

                <button
                        type="button"
                        class="btn btn-outline"
                        onclick="loadProducts()">
                    Refresh
                </button>

            </div>


            <div id="productsContainer">

                <div class="loading">
                    Loading products...
                </div>

            </div>

        </div>


        <!-- ORDERS -->

        <div class="dashboard-panel">

            <div class="panel-header">

                <h2>Orders</h2>

                <button
                        type="button"
                        class="btn btn-outline"
                        onclick="loadOrders()">
                    Refresh
                </button>

            </div>


            <div id="ordersContainer">

                <div class="loading">
                    Loading orders...
                </div>

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
        loadDashboard
    );


    async function loadDashboard() {

        await Promise.all([
            loadUsers(),
            loadProducts(),
            loadOrders()
        ]);
    }


    async function loadUsers() {

        const container =
            document.getElementById(
                "usersContainer"
            );

        try {

            const response =
                await fetch(
                    "../api/admin?action=users"
                );

            const result =
                await response.json();

            if (!response.ok) {

                showError(
                    container,
                    result.message ||
                    "Unable to load users."
                );

                return;
            }

            const users =
                result.data || [];

            document.getElementById(
                "userCount"
            ).textContent =
                users.length;

            displayUsers(users);

        } catch (error) {

            console.error(error);

            showError(
                container,
                "Unable to load users."
            );
        }
    }


    function displayUsers(users) {

        const container =
            document.getElementById(
                "usersContainer"
            );


        if (!users.length) {

            container.innerHTML =
                `<div class="empty-message">
                    No users found.
                 </div>`;

            return;
        }


        let html = `

            <div class="table-wrapper">

                <table class="data-table">

                    <thead>

                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                        </tr>

                    </thead>

                    <tbody>
        `;


        users.forEach(user => {

            html += `

                <tr>

                    <td>
                        ${user.id}
                    </td>

                    <td>
                        ${escapeHtml(user.name)}
                    </td>

                    <td>
                        ${escapeHtml(user.email)}
                    </td>

                    <td>
                        ${escapeHtml(user.role)}
                    </td>

                </tr>

            `;
        });


        html += `

                    </tbody>

                </table>

            </div>
        `;


        container.innerHTML = html;
    }


    async function loadProducts() {

        const container =
            document.getElementById(
                "productsContainer"
            );

        try {

            const response =
                await fetch(
                    "../api/admin?action=products"
                );

            const result =
                await response.json();

            if (!response.ok) {

                showError(
                    container,
                    result.message ||
                    "Unable to load products."
                );

                return;
            }

            const products =
                result.data || [];

            document.getElementById(
                "productCount"
            ).textContent =
                products.length;

            displayProducts(products);

        } catch (error) {

            console.error(error);

            showError(
                container,
                "Unable to load products."
            );
        }
    }


    function displayProducts(products) {

        const container =
            document.getElementById(
                "productsContainer"
            );


        if (!products.length) {

            container.innerHTML =
                `<div class="empty-message">
                    No products found.
                 </div>`;

            return;
        }


        let html = `

            <div class="table-wrapper">

                <table class="data-table">

                    <thead>

                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>

                    </thead>

                    <tbody>
        `;


        products.forEach(product => {

            html += `

                <tr>

                    <td>
                        ${product.id}
                    </td>

                    <td>
                        ${escapeHtml(product.name)}
                    </td>

                    <td>
                        ${escapeHtml(
                            product.category || "-"
                        )}
                    </td>

                    <td>
                        ₹${formatPrice(product.price)}
                    </td>

                    <td>
                        ${product.stock}
                    </td>

                    <td>
                        ${escapeHtml(
                            product.status || "ACTIVE"
                        )}
                    </td>

                    <td>

                        <select
                            onchange="updateProductStatus(
                                ${product.id},
                                this.value
                            )">

                            <option
                                value="ACTIVE"
                                ${product.status === "ACTIVE"
                                    ? "selected"
                                    : ""}>
                                ACTIVE
                            </option>

                            <option
                                value="BLOCKED"
                                ${product.status === "BLOCKED"
                                    ? "selected"
                                    : ""}>
                                BLOCKED
                            </option>

                        </select>

                    </td>

                </tr>

            `;
        });


        html += `

                    </tbody>

                </table>

            </div>
        `;


        container.innerHTML = html;
    }


    async function updateProductStatus(
        productId,
        status
    ) {

        try {

            const response =
                await fetch(
                    "../api/admin?action=product-status",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            productId: productId,
                            status: status
                        })
                    }
                );


            const result =
                await response.json();


            if (!response.ok) {

                alert(
                    result.message ||
                    "Unable to update product status."
                );

                loadProducts();

                return;
            }


            loadProducts();

        } catch (error) {

            console.error(error);

            alert(
                "Unable to update product status."
            );

            loadProducts();
        }
    }


    async function loadOrders() {

        const container =
            document.getElementById(
                "ordersContainer"
            );

        try {

            const response =
                await fetch(
                    "../api/admin?action=orders"
                );

            const result =
                await response.json();

            if (!response.ok) {

                showError(
                    container,
                    result.message ||
                    "Unable to load orders."
                );

                return;
            }

            const orders =
                result.data || [];

            document.getElementById(
                "orderCount"
            ).textContent =
                orders.length;

            displayOrders(orders);

        } catch (error) {

            console.error(error);

            showError(
                container,
                "Unable to load orders."
            );
        }
    }


    function displayOrders(orders) {

        const container =
            document.getElementById(
                "ordersContainer"
            );


        if (!orders.length) {

            container.innerHTML =
                `<div class="empty-message">
                    No orders found.
                 </div>`;

            return;
        }


        let html = `

            <div class="table-wrapper">

                <table class="data-table">

                    <thead>

                        <tr>
                            <th>Order ID</th>
                            <th>Buyer ID</th>
                            <th>Total</th>
                            <th>Payment</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>

                    </thead>

                    <tbody>
        `;


        orders.forEach(order => {

            html += `

                <tr>

                    <td>
                        #${order.id}
                    </td>

                    <td>
                        ${order.buyerId}
                    </td>

                    <td>
                        ₹${formatPrice(
                            order.totalAmount
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            order.paymentStatus || "-"
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            order.status || "PLACED"
                        )}
                    </td>

                    <td>

                        <select
                            onchange="updateOrderStatus(
                                ${order.id},
                                this.value
                            )">

                            <option
                                value="PLACED"
                                ${order.status === "PLACED"
                                    ? "selected"
                                    : ""}>
                                PLACED
                            </option>

                            <option
                                value="CONFIRMED"
                                ${order.status === "CONFIRMED"
                                    ? "selected"
                                    : ""}>
                                CONFIRMED
                            </option>

                            <option
                                value="SHIPPED"
                                ${order.status === "SHIPPED"
                                    ? "selected"
                                    : ""}>
                                SHIPPED
                            </option>

                            <option
                                value="DELIVERED"
                                ${order.status === "DELIVERED"
                                    ? "selected"
                                    : ""}>
                                DELIVERED
                            </option>

                            <option
                                value="CANCELLED"
                                ${order.status === "CANCELLED"
                                    ? "selected"
                                    : ""}>
                                CANCELLED
                            </option>

                        </select>

                    </td>

                </tr>

            `;
        });


        html += `

                    </tbody>

                </table>

            </div>
        `;


        container.innerHTML = html;
    }


    async function updateOrderStatus(
        orderId,
        status
    ) {

        try {

            const response =
                await fetch(
                    "../api/admin?action=order-status",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            orderId: orderId,
                            status: status
                        })
                    }
                );


            const result =
                await response.json();


            if (!response.ok) {

                alert(
                    result.message ||
                    "Unable to update order status."
                );

                loadOrders();

                return;
            }


            loadOrders();

        } catch (error) {

            console.error(error);

            alert(
                "Unable to update order status."
            );

            loadOrders();
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

        } catch (error) {

            console.error(error);

        } finally {

            window.location.href =
                "../index.jsp";
        }
    }


    function formatPrice(value) {

        const number =
            Number(value || 0);

        return number.toFixed(2);
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


    function showError(
        container,
        message
    ) {

        container.innerHTML =
            `<div class="error-message">
                ${escapeHtml(message)}
             </div>`;
    }

</script>

</body>
</html>
