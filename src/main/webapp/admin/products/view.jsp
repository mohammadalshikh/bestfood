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

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

        <style>
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
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">

                <a class="navbar-brand" href="#"> 
                    <img src="/images/logo.png" width="auto" height="40" class="d-inline-block align-top" alt=""/>
                </a>

                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav mr-auto"></ul>
                    <ul class="navbar-nav">
                        <li class="nav-item active"><a class="nav-link" href="/adminhome">Home</a></li>
                        <li class="nav-item active"><a class="nav-link" href="/admin">Logout</a>
                        </li>

                    </ul>
                </div>
            </div>
        </nav>
        <br>
        <div class="d-flex justify-content-end mb-3">
            <a href="/admin/products/add" id="addProduct" class="btn btn-action">Add product</a>
        </div>
        <div class="container-fluid">
            <table class="table">

                <tr>
                    <th scope="col">Serial No.</th>
                    <th scope="col">Product Name</th>
                    <th scope="col">Category</th>
                    <th scope="col">Preview</th>
                    <th scope="col">Quantity</th>
                    <th scope="col">Price</th>
                    <th scope="col">Discount</th>
                    <th scope="col">Weight</th>
                    <th scope="col">Product pair</th>
                    <th scope="col">Suggested Items</th>
                    <th scope="col">Delete</th>
                    <th scope="col">Update</th>
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
                                <td>$<%= String.format("%.2f", product.getPrice()) %></td>
                                <td><%= product.getDiscount() %></td>
                                <td><%= product.getWeight() %></td>
                                <td>
                                    <% int productPairId = product.getProductPair(); 
                                    if (productPairId> 0 && productList != null) {
                                        for (Product p : productList) {
                                            if (p.getId() == productPairId) {
                                                out.print(p.getName());
                                                break;
                                            }
                                        }
                                    } %>
                                </td>
                                <td>
                                    <form action="/suggestItem" method="get">
                                        <label for="suggestedItem"></label>
                                        <select id="suggestedItem" name="suggestedID">
                                            <% int currentItemId = product.getId(); 
                                            int currentSuggestedItem = product.getSuggestedItem();
                                            for (Product p : productList) { 
                                                if (p.getId() != 0 && p.getId() !=currentItemId) { %>
                                                    <option value="<%= p.getId() %>"
                                                        <%=(p.getId()==currentSuggestedItem) ? "selected" : "" %>>
                                                        <%= p.getName() %>
                                                    </option>
                                            <% }} %>

                                        </select>

                                        <input type="hidden" name="productID" value="<%= product.getId() %>">
                                        <input type="submit">
                                    </form>
                                </td>
                                <td>
                                    <form action="products/delete" method="get">
                                        <input type="hidden" name="id" value="<%= product.getId() %>">
                                        <input id="delete" type="submit" value="Delete" class="btn btn-danger">
                                    </form>
                                </td>
                                <td>
                                    <form action="products/update" method="get">
                                        <input type="hidden" name="pid" value="<%= product.getId() %>">
                                        <input type="submit" value="Update" class="btn btn-warning">
                                    </form>
                                </td>
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