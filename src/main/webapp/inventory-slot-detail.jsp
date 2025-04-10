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
                <h5 class="card-title">Slot #${inventorySlot.slotID}</h5>
                <div class="row">
                    <div class="col-md-6">
                        <h6>Character Information</h6>
                        <p><strong>Character Name:</strong> ${character.firstName} ${character.lastName}</p>
                        <p><strong>Character ID:</strong> ${character.characterID}</p>
                        
                        <h6 class="mt-4">Item Information</h6>
                        <p><strong>Item Name:</strong> ${item.itemName}</p>
                        <p><strong>Item Level:</strong> ${item.itemLevel}</p>
                        <p><strong>Quantity:</strong> ${item.quantity}</p>
                        <p><strong>Max Stack Size:</strong> ${item.maxStackSize}</p>
                        <p><strong>Price:</strong> ${item.price}</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="mt-3">
            <a href="inventory-slots" class="btn btn-secondary">Back to Inventory Slots</a>
            <a href="edit-inventory-slot?id=${inventorySlot.slotID}" class="btn btn-warning">Edit Slot</a>
            <a href="delete-inventory-slot?id=${inventorySlot.slotID}" 
               class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this inventory slot?')">Delete Slot</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 