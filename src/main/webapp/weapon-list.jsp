<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Weapons List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Game Weapons</h1>
        <a href="create-weapon" class="btn btn-primary mb-3">Create New Weapon</a>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Item ID</th>
                    <th>Weapon Name</th>
                    <th>Item Level</th>
                    <th>Required Level</th>
                    <th>Damage</th>
                    <th>Attack Speed</th>
                    <th>Weapon Type</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${weapons}" var="weapon">
                    <tr>
                        <td>${weapon.itemID}</td>
                        <td>${weapon.itemName}</td>
                        <td>${weapon.itemLevel}</td>
                        <td>${weapon.requiredLevel}</td>
                        <td>${weapon.damage}</td>
                        <td>${weapon.attackSpeed}</td>
                        <td>${weapon.weaponType}</td>
                        <td>${weapon.price}</td>
                        <td>
                            <a href="weapon?id=${weapon.itemID}" class="btn btn-info btn-sm">View</a>
                            <a href="edit-weapon?id=${weapon.itemID}" class="btn btn-warning btn-sm">Edit</a>
                            <a href="delete-weapon?id=${weapon.itemID}" class="btn btn-danger btn-sm"
                               onclick="return confirm('Are you sure you want to delete this weapon?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 