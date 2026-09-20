<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0">

    <title>Register - BlackMart</title>

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
        <a href="login.jsp">Login</a>
    </nav>

</header>


<main class="auth-page">

    <div class="auth-card">

        <h1>Create Account</h1>

        <p class="auth-subtitle">
            Join BlackMart today
        </p>


        <form id="registerForm">

            <div class="form-group">

                <label for="name">
                    Full Name
                </label>

                <input
                        type="text"
                        id="name"
                        name="name"
                        placeholder="Enter your name"
                        maxlength="100"
                        required>

            </div>


            <div class="form-group">

                <label for="email">
                    Email
                </label>

                <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="Enter your email"
                        maxlength="150"
                        required>

            </div>


            <div class="form-group">

                <label for="password">
                    Password
                </label>

                <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Minimum 6 characters"
                        minlength="6"
                        required>

            </div>


            <div class="form-group">

                <label for="role">
                    Account Type
                </label>

                <select
                        id="role"
                        name="role"
                        required>

                    <option value="BUYER">
                        Buyer
                    </option>

                    <option value="SELLER">
                        Seller
                    </option>

                </select>

            </div>


            <button
                    type="submit"
                    class="btn btn-primary auth-button">

                Create Account

            </button>


            <div
                    id="registerMessage"
                    class="form-message">
            </div>

        </form>


        <p class="auth-footer">

            Already have an account?

            <a href="login.jsp">
                Login here
            </a>

        </p>

    </div>

</main>


<footer class="footer">

    <p>
        © 2026 BlackMart. All rights reserved.
    </p>

</footer>


<script>

    const registerForm =
        document.getElementById("registerForm");

    const registerMessage =
        document.getElementById("registerMessage");


    registerForm.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();

            registerMessage.textContent =
                "Creating account...";


            const name =
                document.getElementById("name")
                    .value
                    .trim();

            const email =
                document.getElementById("email")
                    .value
                    .trim();

            const password =
                document.getElementById("password")
                    .value;

            const role =
                document.getElementById("role")
                    .value;


            try {

                const response =
                    await fetch(
                        "../api/auth?action=register",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({
                                name: name,
                                email: email,
                                password: password,
                                role: role
                            })
                        }
                    );


                const result =
                    await response.json();


                if (!response.ok) {

                    registerMessage.textContent =
                        result.message ||
                        "Registration failed.";

                    return;
                }


                registerMessage.textContent =
                    "Registration successful! Redirecting...";


                setTimeout(
                    function() {

                        window.location.href =
                            "login.jsp";

                    },
                    1000
                );


            } catch (error) {

                console.error(error);

                registerMessage.textContent =
                    "Unable to connect to BlackMart.";
            }

        }
    );

</script>

</body>
</html>
