<%@page import="bestfood.model.Category" %>
<%@page import="java.util.List" %>
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">

        <title>BestFood</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    </head>

    <body>
        <%@ include file="/fragments/admin-navbar.jsp" %>
        <br>
        <div class="jumbotron container border border-info">
            <h3>Add a new product</h3>
            <form action="/admin/products/create" method="post" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-sm-5">
                        List<Category> categoryList = (List<Category>) request.getAttribute("categories"); %>

                        <div class="form-group">
                            <label for="product-name">Name</label>
                            <input type="text" class="form-control border border-warning" required name="product-name" placeholder="Enter name">
                        </div>

                        <div class="form-group">
                            <label for="product-category-id">Select category</label>
                            <select class="form-control border border-warning" name="product-category-id" required>
                                <% if (categoryList != null) { for (Category category : categoryList) { %>
                                    <option value="${ category.id }">
                                        ${ category.name }
                                    </option>
                                    <% } } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="product-price">Price</label>
                            <input type="number" class="form-control border border-warning" required name="product-price" min="1" placeholder="Price">
                        </div>
                        <div class="form-group">
                            <label for="product-weight">Weight in grams</label>
                            <input type="number" class="form-control border border-warning" required name="product-weight" min="1" placeholder="Weight">
                        </div>
                        <div class="form-group">
                            <label for="product-quantity">Available quantity</label>
                            <input type="number" class="form-control border border-warning" required name="product-quantity" min="1" placeholder="Quantity">
                        </div>
                    </div>

                    <div class="col-sm-5"><br>
                        <div class="form-group">
                            <label for="product-description">Description</label>
                            <textarea class="form-control border border-warning" rows="4" name="product-description" placeholder="Product details" value="No product details"></textarea>
                        </div>

                        <p>Image</p>
                        <div class="custom-file">
                            <input type="file" class="form-control border border-warning" required name="product-image-file" id="product-image-file" accept="image/*">
                        </div>

                        <div class="form-group">
                            <img src="" hidden id="product-image-preview" height="100px" width="100px" style="margin-top: 20px">
                        </div>

                        <div class="form-group">
                            <label for="product-discount">Discount</label>
                            <input type="input" class="form-control border border-warning" required name="product-discount" min="0" step="any" placeholder="Discount">
                        </div>

                        <input type="hidden" name="product-image-name">
                        <p></p>
                        <input type="submit" class="btn btn-primary">
                    </div>
                </div>
            </form>
        </div>

        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>

        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>

        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

        <script type="text/javascript">
            var loadFile = function (event) {
                var image = document.getElementById('product-image-preview');
                image.hidden = !image.hidden;
                image.src = URL.createObjectURL(event.target.files[0]);
            };
        </script>
    </body>

</html>