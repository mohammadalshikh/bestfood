<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">

        <title>BestFood</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
        
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap">

        <style>
            body {
                background-image: url("/images/login.jpg");
                background-size: cover;
                background-position: center;
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0;
            }

            .login-wrapper {
                text-align: center;
                position: relative;
            }

            .website-name-wrapper {
                display: flex;
                align-items: center;
                justify-content: center;
                position: absolute;
                top: -130px;
                left: 0;
                right: 0;
            }

            .website-name {
                font-family: 'Pacifico', cursive;
                font-size: 80px;
                font-weight: bold;
                color: #e74c3c;
                white-space: nowrap;
            }

            .login-div {
                max-width: 400px;
                background-color: rgba(255, 255, 255, 0.8);
                padding: 30px;
                border-radius: 10px;
                position: relative;
            }

            .login-div h2 {
                text-align: center;
                margin-bottom: 30px;
            }

            .login-div form .form-group {
                margin-bottom: 20px;
            }

            .login-div form .form-control {
                border-radius: 5px;
            }

            .login-div form .btn-login {
                background-color: #E74B3C;
                color: #fff;
                border: none;
                border-radius: 5px;
                width: 100%;
            }

            .login-div form .btn-login:hover {
                background-color: #D91E18;
            }

            .register-container h2 {
                text-align: center;
                margin-bottom: 30px;
            }

            .register-container form .form-group {
                margin-bottom: 20px;
            }

            .register-container form .form-control {
                border-radius: 5px;
            }

            .error-message {
                color: red;
                font-size: 14px;
                text-align: center;
                margin-top: 10px;
            }

            .register-form {
                display: block;
                margin-top: 20px;
            }

            .modal-dialog {
                top: 5%;
                transform: translateY(-95%);
            }

            .modal-content {
                background-color: white;
                border-radius: 10px;
            }

            .modal-title {
                font-family: 'Pacifico', cursive;
                font-size: 36px;
                color: #e74c3c;
            }

            .modal-body {
                margin-top: -20px;
            }

            .link-control {
                color: #e74c3c;
            }

            .link-control:hover {
                color: #e74c3c;
            }

            .btn-danger {
                background-color: #e74c3c;
            }

            .btn-danger:hover {
                background-color: #D91E18;
            }

            #opt>input::placeholder {
                font-size: 17px;
            }

            #register-submit-button[disabled],
            #register-submit-button[disabled]:hover {
                background-color: #E74B3C;
                color: #fff;
                cursor: default;
            }

            .text-left {
                text-align: left;
            }
        </style>
    </head>

    <body>
        <div class="login-wrapper">
            <div class="website-name-wrapper">
                <h1 class="website-name">BestFood</h1>
            </div>
            <div class="login-div" id="login-div">
                <h2>User Login</h2>
                <form action="/login" method="post">

                    <div class="form-group">
                        <input type="text" name="login-username" id="login-username" placeholder="Username" required class="form-control form-control-lg">
                    </div>

                    <div class="form-group">
                        <input type="password" name="login-password" id="login-password" placeholder="Password" required class="form-control form-control-lg">
                    </div>

                    <p class="error-message">${errorMessage}</p>
                    <button type="submit" class="btn btn-login">Log in</button>
                    <br><br>

                    <span>Don't have an account? <a class="link-control" href="#" data-toggle="modal" data-target="#register-modal">Register</a></span>
                    <br>

                    <span>Admin login page from <a class="link-control" href="/admin/login">here</a></span>
                </form>
            </div>


            <div class="modal fade" id="register-modal" tabindex="-1" role="dialog" aria-labelledby="register-modal-label" aria-hidden="true">

                <div class="modal-dialog" role="document">
                    <div class="modal-content">

                        <div class="modal-header">
                            <h2 class="modal-title" id="register-modal-label">Create an account</h2>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>

                        <div class="modal-body">
                            <form action="/register" method="post" class="register-form" id="register-form">
                                <div class="form-group">
                                    <label for="register-email" style="display: block; width: 100%; text-align: left;">Email address</label>
                                    <input type="email" class="form-control form-control-lg" required minlength="6" required name="register-email" id="register-email" aria-describedby="email-help">
                                    <div class="text-left"><span id="email-error-div" class="error-message"></span></div>
                                </div>

                                <div class="form-group">
                                    <label for="register-username" style="display: block; width: 100%; text-align: left;">Username</label>
                                    <input type="text" name="register-username" id="register-username" required class="form-control form-control-lg">
                                    <div class="text-left"><span id="username-error-div" class="error-message"></span></div>
                                </div>

                                <div class="form-group">
                                    <label for="register-password" style="display: block; width: 100%; text-align: left;">Password</label>
                                    <input type="password" class="form-control form-control-lg" required name="register-password" id="register-password" 
                                    pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*?[~`!@#$%\^&*()\-_=+[\]{};:\x27.,\x22\\|/?><]).{8,}" 
                                    title="Must contain: at least one number, one uppercase letter, one lowercase letter, one special character, and 8 or more characters"
                                    required>

                                    <div style="margin-right: 70%;"><input type="checkbox" onclick="showPassword()">
                                        <p style="display: inline;">Show password</p>
                                    </div>

                                </div>
                                
                                <div id="opt" class="form-group">
                                    <label for="register-address" style="display: block; width: 100%; text-align: left;">Address</label>
                                    <input class="form-control form-control-lg" rows="3" id="register-address" placeholder="Optional" name="register-address"></input>
                                </div>

                                <input id="register-submit-button" type="submit" disabled value="Register" class="btn btn-danger btn-block"><br>
                            </form>  
                        </div>

                    </div>
                </div>

            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>

        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
        
        <script>

            function toggleRegisterform() {
                var loginDiv = document.querySelector('#login-div');
                var registerForm = document.querySelector('#register-form');

                if (loginDiv.style.display === 'none') {
                    loginDiv.style.display = 'block';
                    registerForm.style.display = 'none';
                } else {
                    loginDiv.style.display = 'none';
                    registerForm.style.display = 'block';
                }
            }


            function showPassword() {
                var x = document.getElementById("register-password");
                if (x.type === "password") {
                    x.type = "text";
                } else {
                    x.type = "password";
                }
            }


            function updateRegisterSubmitButtonState() {
                var username = document.getElementById("register-username").value;
                var password = document.getElementById("register-password").value;
                var email = document.getElementById("register-email").value;
                var isValid = true;

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

                if (!password) {
                    isValid = false;
                }

                var usernameErrorDiv = document.getElementById("username-error-div");
                var emailErrorDiv = document.getElementById("email-error-div");
                if (usernameErrorDiv.textContent || emailErrorDiv.textContent) {
                    isValid = false;
                }

                var registerSubmitButton = document.getElementById("register-submit-button");
                registerSubmitButton.disabled = !isValid;
            }
    

            document.getElementById("register-password").addEventListener("input", function () {
                updateRegisterSubmitButtonState();
            });
           
            
            document.getElementById("register-username").addEventListener("blur", function () {

                var username = this.value;

                if (username) {

                    checkUsernameAvailability(username)
                        .then(function (response) {

                            var usernameErrorDiv = document.getElementById("username-error-div");

                            if (response.exists) {
                                usernameErrorDiv.textContent = "Username already exists";
                            } else {
                                usernameErrorDiv.textContent = "";
                            }

                            updateRegisterSubmitButtonState();
                        })
                        .catch(function (error) {
                            console.error("Error checking username availability:", error);
                        });

                } else {

                    document.getElementById("username-error-div").textContent = "";
                    updateRegisterSubmitButtonState();
                }
            });


            document.getElementById("register-email").addEventListener("blur", function () {

                var email = this.value;
                var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

                if (!emailRegex.test(email)) {

                    document.getElementById("email-error-div").textContent = "Invalid email format";
                    updateRegisterSubmitButtonState();
                    return;
                }

                checkEmailAvailability(email)
                    .then(function (response) {

                        var emailErrorDiv = document.getElementById("email-error-div");

                        if (response.exists) {
                            emailErrorDiv.textContent = "Email already exists";
                        } else {
                            emailErrorDiv.textContent = "";
                        }

                        updateRegisterSubmitButtonState();
                    })
                    .catch(function (error) {
                        console.error("Error checking email availability:", error);
                    });

            });
        
            
            function checkUsernameAvailability(username) {
                return $.ajax({
                    type: "GET",
                    url: "/users/check-username",
                    data: { "register-username": username },
                });
            }

            
            function checkEmailAvailability(email) {
                return $.ajax({
                    type: "GET",
                    url: "/users/check-email",
                    data: { "register-email": email },
                });
            }
        
        </script>
    </body>
</html>