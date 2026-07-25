<%@ page import="bestfood.model.CustomCartItem" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>BestFood</title>

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">

        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">

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

            .container {
                flex: 1;
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

            .btn-delete {
                background-color: #e74c3c;
                color: #fff;
                border: none;
                padding: 5px 10px;
                border-radius: 5px;
                cursor: pointer;
                font-size: 14px;
            }

            .btn-action {
                background-color: #2980b9;
                color: #fff;
                border: none;
                padding: 5px 10px;
                border-radius: 5px;
                cursor: pointer;
                font-size: 14px;
                margin-right: 10px;
            }

            .btn-action:hover {
                color: #fff;
            }

            .btn-delete:hover {
                color: #fff;
            }

            .empty-cart-message {
                display: none;
                text-align: center;
                font-size: 18px;
                margin-top: 20px;
            }

            #total {
                font-size: 24px;
                text-align: center;
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


            .disabled-input {
                background-color: transparent;
                border: none;
                pointer-events: none;
                font-size: inherit;
                color: black;
            }
        </style>
    </head>

    <body>
        <div class="bg-image-wrapper">
            <%@ include file="/fragments/navbar.jsp" %>
        </div>

        <div class="container">
            <br><br>
            <h1>My Custom Cart</h1>

            <div class="d-flex justify-content-end mb-3">

                <form action="/custom-cart/clear" method="post">
                    <button type="submit" id="clear-custom-cart-button" class="btn btn-action">
                        Clear cart
                    </button>
                </form>
                <a id="show-cart-button" href="/cart" class="btn btn-action">
                    Show cart
                </a>
                <input id="edit-quantities-button" type="button" value="Edit quantities" class="btn btn-action" onClick="editMode()">

                <button id="confirm-quantities-changes-button" hidden type="submit" form="edit-quantities-form" class="btn btn-action">
                    Confirm changes
                </button>

                <input hidden id="cancel-quantities-changes-button" type="button" value="Cancel" class="btn btn-action" onClick="cancel()">
            </div>
            <br>
            <form action="/custom-cart/items/quantities/update" id="edit-quantities-form" method="post">
                <table class="table" id="custom-cart-table">
                    <thead>
                    <tr>
                        <th></th>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Total price</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                        <% ArrayList<CustomCartItem> customCartItems = (ArrayList<CustomCartItem>) request.getAttribute("customCartItems");
                        for (CustomCartItem item : customCartItems) { %>
                            <tr>
                                <td></td>
                                <td style="width: 250px">
                                    <%= item.getProduct().getName() %>
                                </td>
                                <td style="width: 250px">
                                    <input pattern="[1-9][0-9]*" min="1" style="width: 80px" class="disabled-input" disabled
                                        type="number" name="<%= item.getProduct().getId() %>|quantity"
                                        value="<%= item.getQuantity() %>">

                                    <input type="hidden" name="product-ids" value="<%= item.getProduct().getId() %>">
                                </td>
                                <td style="width: 250px">
                                    $<%= item.getTotalNoTaxNoCoupons() %>
                                </td>
                                <td>
                                    <button type="button" class="btn btn-delete" onclick="removeCustomCartItem('<%= item.getProduct().getId() %>')">
                                        Remove
                                    </button>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </form>

            <form id="remove-custom-cart-item-form" method="post"></form>

            <br>
            <p class="empty-cart-message" id="empty-custom-cart-message">
                Your custom cart is currently empty, add some products to view them here. 
                <br><br>
                <a href="/shop">Go to shop page</a>
            </p>
            <p id="total-no-tax-no-coupons">Total: $${totalNoTaxNoCoupons}</p>
        </div>
        
        <br><br>

        <footer class="footer">
            <p>&copy; 2023 BestFood</p>
            <div>
                <a href="/contact">Contact Us</a>
            </div>
        </footer>

        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

        <script>
            const clearCustomCartButton = document.getElementById("clear-custom-cart-button");
            const showCartButton = document.getElementById("show-cart-button");
            const editQuantitiesButton = document.getElementById("edit-quantities-button");
            const confirmQuantitiesChangesButton = document.getElementById("confirm-quantities-changes-button");
            const cancelQuantitiesChangesButton = document.getElementById("cancel-quantities-changes-button");
            
            const totalNoTaxNoCoupons = document.getElementById("total-no-tax-no-coupons");
            const emptyCustomCartMessage = document.getElementById("empty-custom-cart-message");
            const customCartTable = document.getElementById("custom-cart-table");

            const quantityInputs = document.querySelectorAll('input[name*="|quantity"]');

            function removeCustomCartItem(productId) {
                var form = document.getElementById("remove-custom-cart-item-form");
                form.action = "/custom-cart/items/" + productId + "/remove";
                form.submit();
            }

            if (quantityInputs.length == 0) {
                clearCustomCartButton.style.display = "none";
                customCartTable.style.display = "none";
                emptyCustomCartMessage.style.display = "block";
                totalNoTaxNoCoupons.style.display = "none";
                editQuantitiesButton.hidden = true;
            }

            function editMode() {
                clearCustomCartButton.hidden = true;
                showCartButton.hidden = true;
                editQuantitiesButton.hidden = true;
                confirmQuantitiesChangesButton.hidden = false;
                cancelQuantitiesChangesButton.hidden = false;

                quantityInputs.forEach((input) => {
                    input.removeAttribute("disabled");
                    input.classList.remove("disabled-input");
                    input.style.backgroundColor = "white";
                    input.style.border = "1px solid #ccc";
                });
            }

            function cancel() {
                location.reload();
            }

        </script>
    </body>

</html>