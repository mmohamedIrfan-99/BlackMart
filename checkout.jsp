<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0">

    <title>Checkout - BlackMart</title>

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

</header>


<main>

    <section class="checkout-page">

        <div class="checkout-card">

            <h1>Checkout</h1>

            <p class="auth-subtitle">
                Complete your order
            </p>


            <form id="checkoutForm">

                <div class="form-group">

                    <label for="shippingAddress">
                        Shipping Address
                    </label>

                    <textarea
                            id="shippingAddress"
                            name="shippingAddress"
                            rows="5"
                            maxlength="500"
                            placeholder="Enter your complete shipping address"
                            required></textarea>

                </div>


                <div class="payment-box">

                    <h3>Payment Method</h3>

                    <label class="payment-option">

                        <input
                                type="radio"
                                name="paymentMethod"
                                value="MOCK"
                                checked>

                        Mock Payment

                    </label>

                    <p>
                        This is a project demonstration.
                        No real payment will be processed.
                    </p>

                </div>


                <button
                        type="submit"
                        class="btn btn-primary checkout-button">

                    Place Order

                </button>


                <div
                        id="checkoutMessage"
                        class="form-message">
                </div>

            </form>


            <div class="checkout-links">

                <a
                        href="cart.jsp"
                        class="btn btn-outline">

                    Back to Cart

                </a>

                <a
                        href="products.jsp"
                        class="btn btn-outline">

                    Continue Shopping

                </a>

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

    const checkoutForm =
        document.getElementById("checkoutForm");

    const checkoutMessage =
        document.getElementById("checkoutMessage");


    checkoutForm.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();

            checkoutMessage.textContent =
                "Processing order...";


            const shippingAddress =
                document.getElementById(
                    "shippingAddress"
                ).value.trim();


            if (!shippingAddress) {

                checkoutMessage.textContent =
                    "Shipping address is required.";

                return;
            }


            try {

                const response =
                    await fetch(
                        "../api/orders?action=checkout",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({
                                shippingAddress:
                                    shippingAddress
                            })
                        }
                    );


                const result =
                    await response.json();


                if (!response.ok) {

                    checkoutMessage.textContent =
                        result.message ||
                        "Checkout failed.";

                    return;
                }


                checkoutMessage.textContent =
                    "Order placed successfully!";


                setTimeout(
                    function() {

                        window.location.href =
                            "orders.jsp";

                    },
                    1000
                );


            } catch (error) {

                console.error(error);

                checkoutMessage.textContent =
                    "Unable to process checkout.";
            }

        }
    );

</script>

</body>
</html>
