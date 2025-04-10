<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consumables List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Consumable Items</h1>
        <a href="create-consumable" class="btn btn-primary mb-3">Create New Consumable</a>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Item ID</th>
                    <th>Item Name</th>
                    <th>Item Level</th>
                    <th>Price</th>
                    <th>Duration (seconds)</th>
                    <th>Cooldown (seconds)</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${consumables}" var="consumable">
                    <tr>
                        <td>${consumable.itemID}</td>
                        <td>${consumable.itemName}</td>
                        <td>${consumable.itemLevel}</td>
                        <td>${consumable.price}</td>
                        <td>${consumable.duration}</td>
                        <td>${consumable.cooldown}</td>
                        <td>
                            <a href="consumable?id=${consumable.itemID}" class="btn btn-info btn-sm">View</a>
                            <a href="edit-consumable?id=${consumable.itemID}" class="btn btn-warning btn-sm">Edit</a>
                            <a href="delete-consumable?id=${consumable.itemID}" class="btn btn-danger btn-sm"
                               onclick="return confirm('Are you sure you want to delete this consumable?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 