<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Currency Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Currency Details</h1>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${currency.currencyName}</h5>
                <p class="card-text">
                    <strong>Currency ID:</strong> ${currency.currencyID}<br>
                    <strong>Description:</strong> ${currency.description}<br>
                    <strong>Max Stack Size:</strong> ${currency.maxStackSize}<br>
                    <strong>Weekly Cap:</strong> ${currency.weeklyCap}<br>
                </p>
            </div>
        </div>

        <h2 class="mt-4">Characters with this Currency</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Character ID</th>
                    <th>Character Name</th>
                    <th>Amount</th>
                    <th>Last Updated</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${characterCurrencies}" var="cc">
                    <tr>
                        <td>${cc.characterID}</td>
                        <td>${cc.characterName}</td>
                        <td>${cc.amount}</td>
                        <td>${cc.lastUpdated}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="mt-3">
            <a href="currencies" class="btn btn-secondary">Back to Currencies</a>
            <a href="edit-currency?id=${currency.currencyID}" class="btn btn-warning">Edit Currency</a>
            <a href="delete-currency?id=${currency.currencyID}" class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this currency?')">Delete Currency</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 