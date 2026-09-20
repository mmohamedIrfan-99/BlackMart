<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Seller Dashboard - BlackMart</title>

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
        <a href="products.jsp">Products</a>
        <a href="seller.jsp">Seller Dashboard</a>
        <a href="orders.jsp">Orders</a>
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
                <h1>Seller Dashboard</h1>

                <p>
                    Manage your BlackMart products and orders.
                </p>
            </div>

            <button
                    type="button"
                    class="btn btn-primary"
                    onclick="openProductForm()">
                + Add Product
            </button>

        </div>


        <div class="dashboard-stats">

            <div class="stat-card">

                <h3>Total Products</h3>

                <strong id="productCount">
                    0
                </strong>

            </div>

            <div class="stat-card">

                <h3>My Orders</h3>

                <strong id="orderCount">
                    0
                </strong>

            </div>

        </div>


        <div
                id="productFormContainer"
                class="form-panel"
                style="display:none;">

            <h2 id="formTitle">
                Add Product
            </h2>


            <form id="productForm">

                <input
                        type="hidden"
                        id="productId">


                <div class="form-grid">

                    <div class="form-group">

                        <label for="productName">
                            Product Name
                        </label>

                        <input
                                type="text"
                                id="productName"
                                maxlength="150"
                                required>

                    </div>


                    <div class="form-group">

                        <label for="category">
                            Category
                        </label>

                        <input
                                type="text"
                                id="category"
                                maxlength="100"
                                required>

                    </div>


                    <div class="form-group full-width">

                        <label for="description">
                            Description
                        </label>

                        <textarea
                                id="description"
                                rows="4"
                                maxlength="1000"></textarea>

                    </div>


                    <div class="form-group">

                        <label for="price">
                            Price
                        </label>

                        <input
                                type="number"
                                id="price"
                                min="0.01"
                                step="0.01"
                                required>

                    </div>


                    <div class="form-group">

                        <label for="stock">
                            Stock
                        </label>

                        <input
                                type="number"
                                id="stock"
                                min="0"
                                required>

                    </div>


                    <div class="form-group full-width">

                        <label for="imageUrl">
                            Image URL
                        </label>

                        <input
                                type="url"
                                id="imageUrl"
                                maxlength="500"
                                placeholder="https://example.com/image.jpg">

                    </div>

                </div>


                <div class="form-actions">

                    <button
                            type="submit"
                            class="btn btn-primary">
                        Save Product
                    </button>

                    <button
                            type="button"
                            class="btn btn-outline"
                            onclick="closeProductForm()">
                        Cancel
                    </button>

                </div>


                <div
                        id="productMessage"
                        class="form-message">
                </div>

            </form>

        </div>


        <div class="dashboard-panel">

            <div class="panel-header">

                <h2>My Products</h2>

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


        <div class="dashboard-panel">

            <div class="panel-header">

                <h2>Incoming Orders</h2>

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

    let editingProductId = null;


    document.addEventListener(
        "DOMContentLoaded",
        function() {
            loadProducts();
            loadOrders();
        }
    );


    async function loadProducts() {

        const container =
            document.getElementById(
                "productsContainer"
            );

        try {

            const response =
                await fetch(
                    "../api/products?action=my-products"
                );

            const result =
                await response.json();

            if (!response.ok) {

                container.innerHTML =
                    `<div class="error-message">
                        ${escapeHtml(
                            result.message ||
                            "Unable to load products."
                        )}
                    </div>`;

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

            container.innerHTML =
                `<div class="error-message">
                    Unable to load products.
                 </div>`;
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
                    No products added yet.
                 </div>`;

            return;
        }


        let html =
            `<div class="table-wrapper">
                <table class="data-table">

                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody>`;


        products.forEach(product => {

            html += `

                <tr>

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

                    <td class="table-actions">

                        <button
                                class="btn btn-outline"
                                onclick="editProduct(
                                    ${product.id}
                                )">
                            Edit
                        </button>

                        <button
                                class="btn btn-danger"
                                onclick="deleteProduct(
                                    ${product.id}
                                )">
                            Delete
                        </button>

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


    function openProductForm(product = null) {

        document.getElementById(
            "productFormContainer"
        ).style.display = "block";


        if (product) {

            editingProductId =
                product.id;

            document.getElementById(
                "formTitle"
            ).textContent =
                "Edit Product";

            document.getElementById(
                "productName"
            ).value =
                product.name || "";

            document.getElementById(
                "category"
            ).value =
                product.category || "";

            document.getElementById(
                "description"
            ).value =
                product.description || "";

            document.getElementById(
                "price"
            ).value =
                product.price || "";

            document.getElementById(
                "stock"
            ).value =
                product.stock || 0;

            document.getElementById(
                "imageUrl"
            ).value =
                product.imageUrl || "";

        } else {

            editingProductId = null;

            document.getElementById(
                "formTitle"
            ).textContent =
                "Add Product";

            document.getElementById(
                "productForm"
            ).reset();
        }
    }


    function closeProductForm() {

        editingProductId = null;

        document.getElementById(
            "productFormContainer"
        ).style.display = "none";

        document.getElementById(
            "productForm"
        ).reset();

        document.getElementById(
            "productMessage"
        ).textContent = "";
    }


    async function editProduct(productId) {

        try {

            const response =
                await fetch(
                    `../api/products?action=single&id=${productId}`
                );

            const result =
                await response.json();

            if (!response.ok) {

                alert(
                    result.message ||
                    "Unable to load product."
                );

                return;
            }

            openProductForm(result.data);

        } catch (error) {

            console.error(error);

            alert(
                "Unable to load product."
            );
        }
    }


    document.getElementById(
        "productForm"
    ).addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();


            const message =
                document.getElementById(
                    "productMessage"
                );


            const product = {

                name:
                    document.getElementById(
                        "productName"
                    ).value.trim(),

                description:
                    document.getElementById(
                        "description"
                    ).value.trim(),

                category:
                    document.getElementById(
                        "category"
                    ).value.trim(),

                price:
                    Number(
                        document.getElementById(
                            "price"
                        ).value
                    ),

                stock:
                    Number(
                        document.getElementById(
                            "stock"
                        ).value
                    ),

                imageUrl:
                    document.getElementById(
                        "imageUrl"
                    ).value.trim()
            };


            const action =
                editingProductId
                    ? "update"
                    : "create";


            if (editingProductId) {
                product.id =
                    editingProductId;
            }


            try {

                const response =
                    await fetch(
                        `../api/products?action=${action}`,
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body:
                                JSON.stringify(product)
                        }
                    );


                const result =
                    await response.json();


                if (!response.ok) {

                    message.textContent =
                        result.message ||
                        "Unable to save product.";

                    return;
                }


                message.textContent =
                    "Product saved successfully.";


                setTimeout(
                    function() {

                        closeProductForm();
                        loadProducts();

                    },
                    500
                );


            } catch (error) {

                console.error(error);

                message.textContent =
                    "Unable to save product.";
            }

        }
    );


    async function deleteProduct(productId) {

        if (
            !confirm(
                "Are you sure you want to delete this product?"
            )
        ) {
            return;
        }


        try {

            const response =
                await fetch(
                    `../api/products?action=delete&id=${productId}`,
                    {
                        method: "POST"
                    }
                );


            const result =
                await response.json();


            if (!response.ok) {

                alert(
                    result.message ||
                    "Unable to delete product."
                );

                return;
            }


            loadProducts();

        } catch (error) {

            console.error(error);

            alert(
                "Unable to delete product."
            );
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
                    "../api/orders?action=seller-orders"
                );

            const result =
                await response.json();


            if (!response.ok) {

                container.innerHTML =
                    `<div class="error-message">
                        ${escapeHtml(
                            result.message ||
                            "Unable to load orders."
                        )}
                    </div>`;

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


        if (!orders.length) {

            container.innerHTML =
                `<div class="empty-message">
                    No incoming orders yet.
                 </div>`;

            return;
        }


        let html =
            `<div class="table-wrapper">
                <table class="data-table">

                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Buyer</th>
                            <th>Product</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody>`;


        orders.forEach(order => {

            html += `

                <tr>

                    <td>
                        #${order.orderId}
                    </td>

                    <td>
                        ${escapeHtml(
                            order.buyerName || "-"
                        )}
                    </td>

                    <td>
                        ${escapeHtml(
                            order.productName || "-"
                        )}
                    </td>

                    <td>
                        ${order.quantity || 0}
                    </td>

                    <td>
                        ₹${formatPrice(order.price)}
                    </td>

                    <td>
                        ${escapeHtml(
                            order.status || "PLACED"
                        )}
                    </td>

                    <td>

                        <select
                                onchange="updateOrderStatus(
                                    ${order.orderId},
                                    this.value
                                )">

                            <option value="PLACED"
                                ${order.status === "PLACED"
                                    ? "selected" : ""}>
                                PLACED
                            </option>

                            <option value="CONFIRMED"
                                ${order.status === "CONFIRMED"
                                    ? "selected" : ""}>
                                CONFIRMED
                            </option>

                            <option value="SHIPPED"
                                ${order.status === "SHIPPED"
                                    ? "selected" : ""}>
                                SHIPPED
                            </option>

                            <option value="DELIVERED"
                                ${order.status === "DELIVERED"
                                    ? "selected" : ""}>
                                DELIVERED
                            </option>

                            <option value="CANCELLED"
                                ${order.status === "CANCELLED"
                                    ? "selected" : ""}>
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
                    "../api/orders?action=status",
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
                    "Unable to update order."
                );

                loadOrders();

                return;
            }


            loadOrders();

        } catch (error) {

            console.error(error);

            alert(
                "Unable to update order."
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

</script>

</body>
</html>
