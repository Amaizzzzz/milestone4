<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gear Instance Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Gear Instance Details</h1>
        
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Instance #${gearInstance.instanceID}</h5>
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Gear ID:</strong> ${gearInstance.gearID}</p>
                        <p><strong>Character ID:</strong> ${gearInstance.characterID}</p>
                        <p><strong>Durability:</strong> ${gearInstance.durability}</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="mt-3">
            <a href="gear-instances" class="btn btn-secondary">Back to Gear Instances</a>
            <a href="edit-gear-instance?id=${gearInstance.instanceID}" class="btn btn-warning">Edit Instance</a>
            <a href="delete-gear-instance?id=${gearInstance.instanceID}" 
               class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this gear instance?')">Delete Instance</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 