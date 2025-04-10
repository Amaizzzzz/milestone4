<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inventory Slots</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Inventory Slots</h1>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Character</th>
                    <th>Slot Number</th>
                    <th>Item</th>
                    <th>Quantity</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${inventorySlots}" var="slot">
                    <tr>
                        <td>${slot.character.firstName} ${slot.character.lastName}</td>
                        <td>${slot.slotNumber}</td>
                        <td>${slot.item.itemName}</td>
                        <td>${slot.quantityStacked}</td>
                        <td>
                            <a href="inventory-slot-detail?characterId=${slot.character.characterID}&slotNumber=${slot.slotNumber}" 
                               class="btn btn-info btn-sm">View</a>
                            <a href="edit-inventory-slot?characterId=${slot.character.characterID}&slotNumber=${slot.slotNumber}" 
                               class="btn btn-warning btn-sm">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <div class="mt-3">
            <a href="index.jsp" class="btn btn-secondary">Back to Home</a>
            <a href="add-inventory-slot" class="btn btn-primary">Add New Slot</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 