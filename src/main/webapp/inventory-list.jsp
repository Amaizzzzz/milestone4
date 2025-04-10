<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inventory List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Character Inventories</h1>
        <a href="create-inventory" class="btn btn-primary mb-3">Create New Inventory Slot</a>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Character ID</th>
                    <th>Character Name</th>
                    <th>Slot Number</th>
                    <th>Item ID</th>
                    <th>Item Name</th>
                    <th>Quantity</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${inventorySlots}" var="slot">
                    <tr>
                        <td>${slot.characterID}</td>
                        <td>${slot.characterName}</td>
                        <td>${slot.slotNumber}</td>
                        <td>${slot.itemID}</td>
                        <td>${slot.itemName}</td>
                        <td>${slot.quantity}</td>
                        <td>
                            <a href="inventory?id=${slot.characterID}&slot=${slot.slotNumber}" class="btn btn-info btn-sm">View</a>
                            <a href="edit-inventory?id=${slot.characterID}&slot=${slot.slotNumber}" class="btn btn-warning btn-sm">Edit</a>
                            <a href="delete-inventory?id=${slot.characterID}&slot=${slot.slotNumber}" class="btn btn-danger btn-sm"
                               onclick="return confirm('Are you sure you want to delete this inventory slot?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 