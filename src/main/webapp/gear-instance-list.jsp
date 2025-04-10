<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gear Instances</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Gear Instances</h1>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Instance ID</th>
                    <th>Gear ID</th>
                    <th>Character ID</th>
                    <th>Durability</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${gearInstances}" var="instance">
                    <tr>
                        <td>${instance.instanceID}</td>
                        <td>${instance.gearID}</td>
                        <td>${instance.characterID}</td>
                        <td>${instance.durability}</td>
                        <td>
                            <a href="gear-instance-detail?id=${instance.instanceID}" class="btn btn-info btn-sm">View</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <div class="mt-3">
            <a href="index.jsp" class="btn btn-secondary">Back to Home</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 