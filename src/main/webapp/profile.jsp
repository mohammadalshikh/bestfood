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

            .error-message {
                color: red;
                font-size: 14px;
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
            <%@ include file="/fragments/navbar.jsp" %>
        </div>
        <br>

        <div class="container">
            <div class="col-sm-6 mx-auto">
                <h3 style="margin-top: 10px">User Profile</h3>
                <br>
                <form action="/profile/update" method="post">
                    <div class="form-group">
                        <label for="email">Email address</label>
                        <input type="email" class="form-control form-control-lg" required minlength="6" placeholder="Email*" value="${ email }" name="email" id="email" aria-describedby="email-help">
                        <div class="text-left"><span id="email-error-div" class="error-message"></span></div>

                        <input hidden type="email" class="form-control form-control-lg" required minlength="6" placeholder="Email*" value="${ email }" name="original-email" id="original-email" aria-describedby="email-help">
                    </div>
                
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" name="username" id="username" required placeholder="Username*" value="${ username }" required class="form-control form-control-lg">
                        <div class="text-left"><span id="username-error-div" class="error-message"></span></div>

                        <input hidden type="text" name="original-username" id="original-username" required placeholder="Username*" value="${ username }" required class="form-control form-control-lg">
                    </div>
                
                    <div class="form-group">
                        <label for="address">Address</label>
                        <textarea class="form-control form-control-lg" rows="3" id="address" placeholder="Enter Your Address" name="address">${ address }</textarea>
                        
                        <textarea hidden class="form-control form-control-lg" rows="3" id="original-address" placeholder="Enter Your Address" name="original-address">${ address }</textarea>   
                    </div>
                
                    <div class="form-group">
                        <label>Coupons owned</label>
                        <input class="form-control form-control-lg" readonly="true" value="${ownedCoupons}">
                    </div>
                
                    <div class="form-group">
                        <label>Minimum purchase for next coupon</label>
                        <input class="form-control form-control-lg" readonly="true" value="${untilNextCoupon}">
                    </div>
                
                    <input type="submit" id="update-profile-submit-button" disabled value="Update profile" class="btn btn-danger btn-block"><br>
                
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

            const originalUsername = document.getElementById("original-username").value;
            const originalEmail = document.getElementById("original-email").value;
            const originalAddress = document.getElementById("original-address").value;

            function updateProfileSubmitButtonState() {
                var username = document.getElementById("username").value;
                var email = document.getElementById("email").value;
                var address = document.getElementById("address").value;

                var isValid = true;
                var isDirty = false;

                if (username !== originalUsername || email !== originalEmail || address !== originalAddress) {
                    isDirty = true;
                }

                if (!username) {
                    isValid = false;
                }

                if (!email) {
                    isValid = false;
                } else {
                    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

                    if (!emailRegex.test(email)) {
                        isValid = false;
                        document.getElementById("email-error-div").textContent = "Invalid email format";
                    }
                }

                var usernameErrorDiv = document.getElementById("username-error-div");
                var emailErrorDiv = document.getElementById("email-error-div");

                if (usernameErrorDiv.textContent || emailErrorDiv.textContent) {
                    isValid = false;
                }

                if (!isDirty) {
                    isValid = false;
                }

                document.getElementById("update-profile-submit-button").disabled = !isValid;
            }


            document.getElementById("username").addEventListener("blur", function () {

                var username = this.value;

                if (username) {

                    checkUsernameAvailability(username)
                        .then(function (response) {

                            var usernameErrorDiv = document.getElementById("username-error-div");

                            if (response.exists && username !== originalUsername) {
                                usernameErrorDiv.textContent = "Username already exists";
                            } else {
                                usernameErrorDiv.textContent = "";
                            }

                            updateProfileSubmitButtonState();
                        });

                } else {

                    document.getElementById("username-error-div").textContent = "";
                    updateProfileSubmitButtonState();
                }
            });


            document.getElementById("email").addEventListener("blur", function () {

                var email = this.value;
                var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

                if (!emailRegex.test(email)) {

                    document.getElementById("email-error-div").textContent = "Invalid email format";
                    updateProfileSubmitButtonState();
                    return;
                }

                checkEmailAvailability(email)
                    .then(function (response) {

                        var emailErrorDiv = document.getElementById("email-error-div");

                        if (response.exists && email !== originalEmail) {
                            emailErrorDiv.textContent = "Email already exists";
                        } else {
                            emailErrorDiv.textContent = "";
                        }

                        updateProfileSubmitButtonState();
                    });
            });


            document.getElementById("address").addEventListener("input", function () {
                updateProfileSubmitButtonState();
            });


            function checkUsernameAvailability(username) {
                return $.ajax({
                    type: "GET",
                    url: "/users/check-username",
                    data: { "register-username": username }
                });
            }


            function checkEmailAvailability(email) {
                return $.ajax({
                    type: "GET",
                    url: "/users/check-email",
                    data: { "register-email": email }
                });
            }

        </script>
    </body>
</html>