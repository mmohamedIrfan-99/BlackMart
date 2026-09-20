<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0">

    <title>Login - BlackMart</title>

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
        <a href="register.jsp">Register</a>
    </nav>

</header>


<main class="auth-page">

    <div class="auth-card">

        <h1>Welcome Back</h1>

        <p class="auth-subtitle">
            Login to your BlackMart account
        </p>


        <form id="loginForm">

            <div class="form-group">

                <label for="email">
                    Email
                </label>

                <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="Enter your email"
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
                        placeholder="Enter your password"
                        required>

            </div>


            <button
                    type="submit"
                    class="btn btn-primary auth-button">

                Login

            </button>


            <div
                    id="loginMessage"
                    class="form-message">
            </div>

        </form>


        <p class="auth-footer">

            Don't have an account?

            <a href="register.jsp">
                Create an account
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

    const loginForm =
        document.getElementById("loginForm");

    const loginMessage =
        document.getElementById("loginMessage");


    loginForm.addEventListener(
        "submit",
        async function(event) {

            event.preventDefault();

            loginMessage.textContent =
                "Logging in...";


            const email =
                document.getElementById("email")
                    .value
                    .trim();

            const password =
                document.getElementById("password")
                    .value;


            try {

                const response =
                    await fetch(
                        "../api/auth?action=login",
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({
                                email: email,
                                password: password
                            })
                        }
                    );


                const result =
                    await response.json();


                if (!response.ok) {

                    loginMessage.textContent =
                        result.message ||
                        "Login failed.";

                    return;
                }


                loginMessage.textContent =
                    "Login successful!";


                const user =
                    result.data;


                setTimeout(
                    function() {

                        if (
                            user &&
                            user.role === "ADMIN"
                        ) {

                            window.location.href =
                                "admin.jsp";

                        } else if (
                            user &&
                            user.role === "SELLER"
                        ) {

                            window.location.href =
                                "seller.jsp";

                        } else {

                            window.location.href =
                                "../index.jsp";
                        }

                    },
                    500
                );


            } catch (error) {

                console.error(error);

                loginMessage.textContent =
                    "Unable to connect to BlackMart.";
            }

        }
    );

</script>

</body>
</html>
