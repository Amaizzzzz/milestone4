<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inventory Slot Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Inventory Slot Details</h1>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Slot ${inventorySlot.slotNumber} - ${character.firstName} ${character.lastName}</h5>
                <p class="card-text">
                    <strong>Character ID:</strong> ${inventorySlot.characterID}<br>
                    <strong>Slot Number:</strong> ${inventorySlot.slotNumber}<br>
                    <strong>Item ID:</strong> ${inventorySlot.itemID}<br>
                    <strong>Item Name:</strong> ${inventorySlot.itemName}<br>
                    <strong>Quantity:</strong> ${inventorySlot.quantity}<br>
                </p>
            </div>
        </div>

        <h2 class="mt-4">Item Details</h2>
        <div class="card">
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty item}">
                        <p class="card-text">
                            <strong>Item Level:</strong> ${item.itemLevel}<br>
                            <strong>Max Stack Size:</strong> ${item.maxStackSize}<br>
                            <strong>Price:</strong> ${item.price}<br>
                            <c:if test="${not empty item.description}">
                                <strong>Description:</strong> ${item.description}<br>
                            </c:if>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <p class="card-text">No item in this slot</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="mt-3">
            <a href="inventory?id=${inventorySlot.characterID}" class="btn btn-secondary">Back to Inventory</a>
            <a href="edit-inventory?id=${inventorySlot.characterID}&slot=${inventorySlot.slotNumber}" class="btn btn-warning">Edit Slot</a>
            <a href="delete-inventory?id=${inventorySlot.characterID}&slot=${inventorySlot.slotNumber}" class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this inventory slot?')">Delete Slot</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 