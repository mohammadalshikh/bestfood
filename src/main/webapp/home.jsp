<%@page import="bestfood.model.Product" %>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>BestFood</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">

        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">

        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">

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

            .hero-section {
                text-align: center;
                padding: 120px 0;
            }

            .hero-text {
                font-size: 36px;
                font-weight: bold;
                text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
                margin-bottom: 20px;
                color: white;
            }

            .search-container {
                max-width: 500px;
                margin: 0 auto;
                text-align: center;
                position: relative;
            }

            .search-input {
                width: 100%;
                padding: 15px;
                border: 1px solid #ccc;
                border-radius: 30px;
                font-size: 16px;
                outline: none;
            }

            #searchResults {
                position: absolute;
                z-index: 1;
                background-color: #fff;
                border: 1px solid #ddd;
                width: 100%;
                max-height: 200px;
                overflow-y: auto;
                display: none;
                list-style: none;
                padding-left: 0;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            #searchResults li {
                padding: 8px;
                cursor: pointer;
                color: #292929;
            }

            #searchResults li:hover {
                background-color: #f2f2f2;
            }

            #viewAllBtn {
                display: none;
            }

            .search-btn {
                background-color: #e74c3c;
                color: #fff;
                border: none;
                border-radius: 30px;
                padding: 15px 30px;
                font-size: 16px;
                cursor: pointer;
                margin-top: 20px;
                transition: background-color 0.3s ease;
            }

            .search-btn:hover {
                color: white;
                text-decoration: none;
                background-color: #D91E18;
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
            <section class="hero-section">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12">
                            <h1 class="hero-text">Food Ecommerce Simplified</h1>
                            <div class="search-container">
                                <input type="text" class="search-input" placeholder="Search products..." id="searchInput">
                                <ul style="color: #292929" id="searchResults"></ul>
                                <a class="search-btn" href="#" id="viewAllBtn">View all results</a>
                                <br><br><br><br>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        
        <br>
        <section class="restaurant-section">
            <div class="container">
                <div class="row">
        
                    <% List<Product> products = (List<Product>) request.getAttribute("products");
                    int count = 0;
                    if (products != null) {
                        for (Product product : products) {
                            if (count >= 3) {
                                break;
                            }
                            count++; %>
                            <div class="col-md-4">
                                <div class="restaurant-item">
                                    <img src="<%= product.getImage() %>" alt="<%= product.getName() %>">
                                    <div class="container">
                                        <br>
                                        <h4>
                                            <%= product.getName() %> - <%= product.getPrice() %>$
                                        </h4>
        
                                        <form action="/cart/items" method="post">
                                            <input hidden type="number" name="product-id" value="<%= product.getId() %>">
                                            <input hidden type="number" name="product-quantity" value="1">
                                        
                                            <button style="background-color: #E74B3C; border-color: #E74B3C;" type="submit" class="btn btn-primary btn-lg">
                                                <i class="fas fa-shopping-cart"></i>
                                                Add to Cart
                                            </button>
                                        </form>
        
                                        <br>
        
                                        <form action="/custom-cart/items" method="post">
                                            <input hidden type="number" name="product-id" value="<%= product.getId() %>">
                                            <input hidden type="number" name="product-quantity" value="1">

                                            <button style="background-color: #027BFF; font-size: 14px;" type="submit" class="btn btn-primary btn-lg">
                                                <i class="fas fa-shopping-cart"></i>
                                                Add to Custom Cart
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                    <% } } %>
                </div>
            </div>
        </section>

        <footer class="footer">
            <p>&copy; 2023 BestFood</p>
            <div>
                <a href="/contact">Contact Us</a>
            </div>
        </footer>

        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
        
        <script>
            const searchInput = document.getElementById('searchInput');
            const searchResults = document.getElementById('searchResults');

            searchInput.addEventListener('input', handleSearch);

            function handleSearch() {
                const query = searchInput.value.trim();

                if (query === '') {
                    searchResults.style.display = 'none';
                    return;
                }

                fetch('/search?query=' + encodeURIComponent(query))
                    .then(response => response.json())
                    .then(products => {
                        displaySearchResults(products);
                    });
            }


            function displaySearchResults(products) {

                searchResults.innerHTML = '';
                if (products.length > 0) {
                    for (let i = 0; i < Math.min(products.length, 5); i++) {

                        const product = products[i];
                        const li = document.createElement('li');
                        li.textContent = product.name + " - $" + product.price;
                        li.dataset.id = product.id;
                        searchResults.appendChild(li);

                    }
                    searchResults.style.display = 'block';

                } else {
                    const li = document.createElement('li');
                    li.textContent = "No results found.";
                    searchResults.appendChild(li);
                    searchResults.style.display = 'block';

                }
            }

            searchResults.addEventListener('click', function (event) {

                if (event.target.tagName === 'LI') {

                    const productId = event.target.dataset.id;

                    if (productId) {
                        const form = document.createElement("form");
                        form.method = "POST";
                        form.action = "/cart/items";

                        const productInput = document.createElement("input");
                        productInput.type = "hidden";
                        productInput.name = "product-id";
                        productInput.value = productId;

                        const quantityInput = document.createElement("input");
                        quantityInput.type = "hidden";
                        quantityInput.name = "product-quantity";
                        quantityInput.value = "1";

                        form.appendChild(productInput);
                        form.appendChild(quantityInput);

                        document.body.appendChild(form);
                        form.submit();
                    }
                }
            });

            document.addEventListener('click', function (event) {

                const target = event.target;

                if (!target.matches('.search-input')
                    && !target.matches('#searchResults li')) {

                    searchResults.style.display = 'none';
                }
            });

            searchInput.addEventListener('keypress', function (event) {
                
                if (event.key === 'Enter') {
                    event.preventDefault();
                }
            });
        </script>

        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>

        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>

        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    </body>

</html>