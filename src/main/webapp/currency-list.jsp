<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Currencies List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Game Currencies</h1>
        <a href="create-currency" class="btn btn-primary mb-3">Create New Currency</a>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Currency ID</th>
                    <th>Currency Name</th>
                    <th>Description</th>
                    <th>Max Stack Size</th>
                    <th>Weekly Cap</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${currencies}" var="currency">
                    <tr>
                        <td>${currency.currencyID}</td>
                        <td>${currency.currencyName}</td>
                        <td>${currency.description}</td>
                        <td>${currency.maxStackSize}</td>
                        <td>${currency.weeklyCap}</td>
                        <td>
                            <a href="currency?id=${currency.currencyID}" class="btn btn-info btn-sm">View</a>
                            <a href="edit-currency?id=${currency.currencyID}" class="btn btn-warning btn-sm">Edit</a>
                            <a href="delete-currency?id=${currency.currencyID}" class="btn btn-danger btn-sm"
                               onclick="return confirm('Are you sure you want to delete this currency?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 