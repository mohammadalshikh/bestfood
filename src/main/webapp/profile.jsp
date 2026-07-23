<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>BestFood</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">

        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap">

        <style>
            body {
                font-family: 'Roboto', sans-serif;
                background-color: #F8F9FA;
            }

            html,
            body {
                height: 100%;
            }

            .bg-image-wrapper {
                background-image: url('/images/bg.jpg');
                background-size: cover;
                background-repeat: no-repeat;
                background-position: center top;
            }

            .navbar {
                background-color: transparent;
                font-weight: 500;
                font-size: 17px;
            }

            .navbar-brand {
                font-family: 'Pacifico', cursive;
                font-size: 28px;
                color: #fff;
            }

            .navbar-brand:hover {
                font-family: 'Pacifico', cursive;
                font-size: 28px;
                color: #e74c3c;
            }

            .navbar-nav .nav-link {
                color: #fff;
                transition: 0.5s ease;
            }

            .navbar-nav .nav-link:hover {
                color: #e74c3c;
                font-weight: bold;
            }

            .footer {
                background-color: #292929;
                color: #fff;
                text-align: center;
                padding: 15px;
                font-family: 'Segoe UI', sans-serif;
                font-size: 14px;
            }

            .footer a {
                color: #fff;
                font-weight: bold;
                text-decoration: none;
                margin: 5px;
            }

            .footer a:hover {
                color: #e74c3c;
            }
        </style>
    </head>

    <body>
        <div class="bg-image-wrapper">
            <nav class="navbar navbar-expand-lg">
                <div class="container">
                    <a class="navbar-brand" href="/index">BestFood</a>

                    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>

                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="navbar-nav ml-auto">
                            <li class="nav-item">
                                <a class="nav-link" href="/index">Home</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/shop">Shop</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/cart">Cart</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/profileDisplay">Profile</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/">Logout</a>
                            </li>
                        </ul>
                    </div>

                </div>
            </nav>
        </div>
        <br>

        <div class="container">
            <div class="col-sm-6 mx-auto">
                <h3 style="margin-top: 10px">User Profile</h3>
                <br>
                <form action="updateuser" method="post">
                    <div class="form-group">
                        <label for="email">Email address</label>
                        <input type="email" class="form-control form-control-lg" required minlength="6" placeholder="Email*" value="${email}" required name="email" id="email" aria-describedby="emailHelp">
                    </div>

                    <div class="form-group">
                        <label for="firstName">Username</label>
                        <input type="hidden" name="userid" value="${userid}">
                        <input type="text" name="username" id="firstName" required placeholder="Your Username*" value="${username}" required class="form-control form-control-lg">
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>

                        <input type="password" class="form-control form-control-lg" required placeholder="Password*" 
                        value="${password }" required name="password" id="password"
                        pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*?[~`!@#$%\^&*()\-_=+[\]{};:\x27.,\x22\\|/?><]).{8,}"
                        title="Must contain: at least one number, one uppercase letter, one lowercase letter, one special character, and 8 or more characters" required>

                        <input type="checkbox" onclick="showPassword()"> Show password
                    </div>

                    <div class="form-group">
                        <label for="address">Address</label>
                        <textarea class="form-control form-control-lg" rows="3" placeholder="Enter Your Address" name="address">${address}</textarea>
                    </div>

                    <div class="form-group">
                        <label>Coupons Owned</label>
                        <input class="form-control form-control-lg" readonly="true" value="${userCoupons}">
                    </div>

                    <div class="form-group">
                        <label>Minimum purchase for next coupon</label>
                        <input class="form-control form-control-lg" readonly="true" value="${100-cumulativeTotal}">
                    </div>

                    <input type="submit" value="Update Profile" class="btn btn-primary btn-block"><br>

                </form>
            </div>
        </div>

        <br> <br>
        <footer class="footer">
            <p>&copy; 2023 BestFood</p>
            <div>
                <a href="/contact">Contact Us</a>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>

        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>

        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

        <script>
            function showPassword() {
                var x = document.getElementById("password");

                if (x.type === "password") {
                    x.type = "text";
                }
                else {
                    x.type = "password";
                }
            }
        </script>
    </body>
</html>