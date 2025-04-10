<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consumable Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Consumable Details</h1>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${consumable.itemName}</h5>
                <p class="card-text">
                    <strong>Item ID:</strong> ${consumable.itemID}<br>
                    <strong>Item Level:</strong> ${consumable.itemLevel}<br>
                    <strong>Max Stack Size:</strong> ${consumable.maxStackSize}<br>
                    <strong>Price:</strong> ${consumable.price}<br>
                    <strong>Duration:</strong> ${consumable.duration} seconds<br>
                    <strong>Cooldown:</strong> ${consumable.cooldown} seconds<br>
                </p>
            </div>
        </div>

        <h2 class="mt-4">Stat Bonuses</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Stat Type</th>
                    <th>Bonus Value</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${consumableStats}" var="stat">
                    <tr>
                        <td>${stat.statType}</td>
                        <td>${stat.bonusValue}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="mt-3">
            <a href="consumables" class="btn btn-secondary">Back to Consumables</a>
            <a href="edit-consumable?id=${consumable.itemID}" class="btn btn-warning">Edit Consumable</a>
            <a href="delete-consumable?id=${consumable.itemID}" class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this consumable?')">Delete Consumable</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 