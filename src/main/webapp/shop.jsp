<%@ page import="bestfood.model.Product" %>
<%@ page import="java.util.ArrayList" %>
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


            .restaurant-section {
                padding: 40px 0;
            }

            .restaurant-item {
                background-color: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                padding: 20px;
                margin-bottom: 30px;
                text-align: center;
            }

            .restaurant-item img {
                width: 100%;
                aspect-ratio: 3 / 2;
                object-fit: contain;
                border-radius: 10px;
            }

            .restaurant-item h4 {
                font-size: 24px;
                font-weight: bold;
                margin-bottom: 10px;
            }

            .restaurant-item p {
                font-size: 16px;
                color: #555;
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

        <% ArrayList<Product> products = (ArrayList<Product>) request.getAttribute("products"); %>
        <section class="restaurant-section">
            <div class="container">
                <div class="row">
                    <% for (Product product : products) { %>
                        <div class="col-md-4">
                            <div class="restaurant-item">
                                <img src="<%= product.getImage() %>" alt="<%= product.getName() %>">
                                <div class="container">
                                    <br>
                                    <h4><%= product.getName() %> - <%= product.getPrice() %>$</h4>
                                        <% if (product.getBoughtWithProduct() != null) { %>
                                            <p>Buy with <%= product.getBoughtWithProduct().getName() %></p>
                                        <% } %>
                                    <form action="/cart/items" method="post" id="<%= product.getId() %>|ac">
                                        <input hidden type="number" name="product-id" value="<%= product.getId() %>">
                                        <input hidden type="number" name="product-quantity" value="1">
                                        <button style="background-color: #E74B3C; border-color: #E74B3C;" type="submit" class="btn btn-primary btn-lg">
                                            <i class="fas fa-shopping-cart"></i> Add to Cart
                                        </button>
                                    </form>
                                    <br>
                                    <form action="/custom-cart/items" method="post" id="<%= product.getId() %>|acc">
                                        <input hidden type="number" name="product-id" value="<%= product.getId() %>">
                                        <input hidden type="number" name="product-quantity" value="1">
                                        <button style="background-color: #027BFF; font-size: 14px;" type="submit" class="btn btn-primary btn-lg">
                                            <i class="fas fa-shopping-cart"></i> Add to Custom Cart
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    <% } %>
                    
                </div>
            </div>
        </section>
        <br>

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