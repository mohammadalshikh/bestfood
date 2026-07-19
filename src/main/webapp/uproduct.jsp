<%@page import="bestfood.model.Product" %>
<%@page import="bestfood.model.Category" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">

        <title>BestFood</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    </head>

    <body class="bg-light">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#"> 
                    <img src="/images/logo.png" width="auto" height="40" class="d-inline-block align-top" alt="" />
                </a>
                <button class="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav mr-auto"></ul>
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a class="nav-link" href="/index">Home</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="/profileDisplay">Profile</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" th:href="@{/shop}" href="#">Shop</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" th:href="@{/cart}" href="#">Cart</a>
                        </li>
                        <li class="nav-item active">
                            <span class="nav-link" th:text="${cartCount}">0</span>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" sec:authorize="isAuthenticated()" href="/">Logout</a>
                        </li>
                    </ul>

                </div>
            </div>
        </nav>
        <div class="container-fluid">


            <table class="table">
                <tr>
                    <th scope="col">Serial No.</th>
                    <th scope="col">Product Name</th>
                    <th scope="col">Category</th>
                    <th scope="col">Preview</th>
                    <th scope="col">Quantity</th>
                    <th scope="col">Price</th>
                    <th scope="col">Weight</th>
                    <th scope="col">Descrption</th>
                    <th scope="col">Buy</th>
                </tr>
                <tbody>
                    <% List<Product> productList = (List<Product>) request.getAttribute("products");
                    List<Category> categoryList = (List<Category>) request.getAttribute("categories");
                    Map<Integer, String> categoryMap = new HashMap<>();
                    if (categoryList != null) {
                        for (Category cat : categoryList) {
                            categoryMap.put(cat.getCategoryId(), cat.getName());
                        }
                    }

                    if (productList != null) {
                        for (Product product : productList) { %>
                            <tr>
                                <td><%= product.getId() %></td>
                                <td><%= product.getName() %></td>
                                <td><%= categoryMap.getOrDefault(product.getCategoryId(), "") %></td>
                                <td><img src="<%= product.getImage() %>" height="100px" width="100px"></td>
                                <td><%= product.getQuantity() %></td>
                                <td><%= String.format("%.2f", product.getPrice()) %></td>
                                <td><%= product.getWeight() %></td>
                                <td><%= product.getDescription() %></td>

                                <td>
                                    <form action="/buy" method="get">

                                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-cart" viewBox="0 0 16 16">
                                            <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l1.313 7h8.17l1.313-7H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z" />
                                        </svg>

                                        <input type="hidden" name="id" value="<%= product.getId() %>">
                                        <input type="submit" value="Buy" class="btn btn-info btn-lg">
                                    </form>
                                </td>

                                <td></td>
                            </tr>
                    <% } } %>

                </tbody>
            </table>
        </div>


        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>

        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>

        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    </body>
</html>