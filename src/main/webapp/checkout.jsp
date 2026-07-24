<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>BestFood</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">

        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" >

        <style>
            body {
                font-family: 'Roboto', sans-serif;
                background-color: #F8F9FA;
                display: flex;
                flex-direction: column;
            }

            html, body {
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

            .container {
                display: flex;
                justify-content: space-between;
                flex-wrap: wrap;
            }

            .left-content {
                flex: 1;
                max-width: 300px;
                padding: 10px;
            }

            .form-container {
                flex: 1;
                max-width: 500px;
                padding: 20px;
                background-color: #fff;
                border-radius: 5px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                margin-left: 200px;
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
        <br> <br>

        <div class="container d-flex justify-content-center">
            <div class="left-content">
                <H1 style="white-space: nowrap">Order summary</H1>
                <br>
                <H4>Items: $${totalNoTaxNoCoupons}</H4>
                <br>
                <H4 style="white-space: nowrap">Shipping & handling: FREE</H4>
                <br>
                <H4>Total after tax: $${totalAfterTaxNoCoupons}</H4>
                <br>
                <H4>-------------------</H4>
                <br> <br>
                <H4>Order total: $${totalFinal}</H4>
                <br>
                <form action="/checkout/coupons" method="post">
                    <label>Apply coupons</label>
                    <input type="number" class="form-control" name="apply" placeholder="${couponsApplied}" max="${couponsOwned}" min="0">
                    <br>
                    
                    <button type="submit" class="btn btn-primary">Apply</button>
                </form>
            </div>
            <div class="form-container">
                <form id="payment-form" action="/checkout" method="post">
                    <div class="form-group">
                        <label for="first-name">First name</label>
                        <input type="text" class="form-control" id="first-name" value="Khaled" required>
                    </div>
                    <div class="form-group">
                        <label for="last-name">Last name</label>
                        <input type="text" class="form-control" id="last-name" value="Jobabo" required>
                    </div>
                    <div class="form-group">
                        <label for="card-number">Card number</label>
                        <input type="text" class="form-control" id="card-number" value="5258 9760 0172 6101" required>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="expiry-month">Card expiry date (MM/YY)</label>
                            <input type="text" class="form-control" id="expiry-month" value="06/25" required>
                        </div>
                        <div class="form-group">
                            <label for="cvv">CVV</label>
                            <input type="password" class="form-control" id="cvv" value="338" required maxlength="3">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="address">Address</label>
                        <input type="text" class="form-control" id="address" value="40 Guy St" required>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="city">City</label>
                            <input type="text" class="form-control" id="city" value="Montreal" required>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="postal-code">Postal code</label>
                            <input type="text" class="form-control" id="postal-code" value="H4N1G2" required
                                maxlength="6">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email">Email address</label>
                        <input type="email" class="form-control" id="email" value="bestfood354@gmail.com" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit payment</button>
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

        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>

        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
    </body>

</html>