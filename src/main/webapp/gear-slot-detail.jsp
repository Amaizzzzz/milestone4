<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gear Slot Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Gear Slot Details</h1>
        
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Slot #${gearSlot.slotID}</h5>
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Slot Name:</strong> ${gearSlot.slotName}</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="mt-3">
            <a href="gear-slots" class="btn btn-secondary">Back to Gear Slots</a>
            <a href="edit-gear-slot?id=${gearSlot.slotID}" class="btn btn-warning">Edit Slot</a>
            <a href="delete-gear-slot?id=${gearSlot.slotID}" 
               class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this gear slot?')">Delete Slot</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 