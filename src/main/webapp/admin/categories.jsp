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

    <body class="bg-light">
        <%@ include file="/fragments/admin-navbar.jsp" %>
        <br>
        <div class="container">

            <button type="button" style="margin: 20px 0" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenter">
                Add category
            </button>

            <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <form action="/admin/categories" method="post">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLongTitle">Add new category</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>

                            <div class="modal-body text-center">
                                <input type="text" name="category-name" class="form-control" id="category-name" required="required" placeholder="Category name">
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <input type="submit" value="Save Changes" class="btn btn-primary">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <br>


            <table class="table">
                <thead class="thead-light">
                    <tr>
                        <th scope="col">SN</th>
                        <th scope="col">Category Name</th>
                        <th scope="col">Delete</th>
                        <th scope="col">Update</th>
                    </tr>
                </thead>
                <tbody>
                    <% List<Category> categoryList = (List<Category>) request.getAttribute("categories"); 
                    if (categoryList != null) {
                        for (Category category : categoryList) { %>
                            <tr>
                                <td><%= category.getId() %></td>
                                <td><%= category.getName() %></td>

                                <td>
                                    <form action="/admin/categories/${category.id}/delete" method="post">
                                        <input type="submit" value="Delete" class="btn btn-danger">
                                    </form>
                                </td>
                                <td>
                                    <form action="/admin/categories/${category.id}" method="post">
                                        <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#exampleModalCenter2" 
                                        onclick="document.getElementById('category-name').value = '<%= category.getName() %>'; 
                                        document.getElementById('categoryid').value =  '<%= category.getId() %>'; ">Update
                                        </button>

                                        <div class="modal fade" id="exampleModalCenter2" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                                            
                                            <div class="modal-dialog modal-dialog-centered" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="exampleModalLongTitle">
                                                            Update product details
                                                        </h5>
                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                            <span aria-hidden="true">&times;</span>
                                                        </button>
                                                    </div>
                                                    <div class="modal-body text-center">
                                                        <div class="form-group">
                                                            <input class="form-control" type="number" readonly="readonly" name="category-id" id="category-id" value="0">
                                                        </div>
                                                        <div class="form-group">
                                                            <input class="form-control" type="text" name="category-name" id="category-name" value="category-name">
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                                            Close
                                                        </button>
                                                        <button type="submit" class="btn btn-primary">
                                                            Apply changes
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
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