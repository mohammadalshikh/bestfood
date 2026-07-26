<%@page import="bestfood.model.Product" %>
<%@page import="bestfood.model.Category" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">

        <title>BestFood</title>

        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
        
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">

        <style>
            body {
                font-family: 'Roboto', sans-serif;
            }

            .navbar {
                background-color: #343a40;
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
                color: #3c7ff3;
            }

            .navbar-nav .nav-link {
                color: #fff;
                transition: 0.5s ease;
            }

            .navbar-nav .nav-link:hover {
                color: #3c7ff3;
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

            .btn-action {
                background-color: #2980b9;
                color: #fff;
                border: none;
                padding: 5px 10px;
                border-radius: 5px;
                cursor: pointer;
                font-size: 14px;
                margin-right: 100px;
            }

            .btn-action:hover {
                color: #fff;
            }
        </style>
    </head>

    <body class="bg-light">
        <%@ include file="/fragments/admin-navbar.jsp" %>
        <br>
        <div class="d-flex justify-content-end mb-3">
            <a href="/admin/products/create" id="create-product-button" class="btn btn-action">Add product</a>
        </div>
        <div class="container-fluid">
            <table class="table">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Product name</th>
                    <th scope="col">Category</th>
                    <th scope="col">Preview</th>
                    <th scope="col">Quantity</th>
                    <th scope="col">Price</th>
                    <th scope="col">Discount</th>
                    <th scope="col">Weight</th>
                    <th scope="col">Buy with</th>
                    <th scope="col">Update</th>
                </tr>
                <tbody>
                    <% List<Product> products = (List<Product>) request.getAttribute("products");
                    List<Category> categories = (List<Category>) request.getAttribute("categories");

                    Map<Integer, String> categoryMap = new HashMap<>();
                    if (categories != null) {
                        for (Category category : categories) {
                            categoryMap.put(category.getId(), category.getName());
                        }
                    }

                    if (products != null) {
                        for (Product product : products) { 
                            if (product.getId() == 1) { %>
                                <tr>
                                    <td><%= product.getId() %></td>
                                    <td><%= product.getName() %></td>
                                    <td><%= categoryMap.getOrDefault(product.getCategory().getId(), "") %></td>
                                    <td><img src="<%= product.getImage() %>" height="100px" width="100px"></td>
                                    <td><%= product.getQuantity() %></td>
                                    <td>$<%= String.format("%.2f", product.getPrice()) %></td>
                                    <td><%= product.getDiscount() %></td>
                                    <td><%= product.getWeight() %></td>
                                    <td>
                                        <% if (product.getBoughtWithProduct() != null) { %>
                                            <%= product.getBoughtWithProduct().getName() %>
                                        <% } else { %>
                                            -
                                        <% } %>
                                    </td>
                                    <td>
                                    </td>
                                </tr>
                            <% } else { %>
                                <tr>
                                    <td><%= product.getId() %></td>
                                    <td><%= product.getName() %></td>
                                    <td><%= categoryMap.getOrDefault(product.getCategory().getId(), "") %></td>
                                    <td><img src="<%= product.getImage() %>" height="100px" width="100px"></td>
                                    <td><%= product.getQuantity() %></td>
                                    <td>$<%= String.format("%.2f", product.getPrice()) %></td>
                                    <td><%= product.getDiscount() %></td>
                                    <td><%= product.getWeight() %></td>
                                    <td>
                                        <% if (product.getBoughtWithProduct() != null) { %>
                                            <%= product.getBoughtWithProduct().getName() %>
                                        <% } else { %>
                                            -
                                        <% } %>
                                    </td>
                                    <td>
                                        <form action="/admin/products/<%= product.getId() %>/update" method="get">
                                            <input type="submit" value="Update" class="btn btn-primary">
                                        </form>
                                    </td>
                                </tr>   

                    <% } } } %>
                </tbody>
            </table>
        </div>


        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
        
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>

        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    </body>

</html>