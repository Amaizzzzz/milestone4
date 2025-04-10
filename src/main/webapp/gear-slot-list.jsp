<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gear Slots</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Gear Slots</h1>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Slot ID</th>
                    <th>Slot Name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${gearSlots}" var="slot">
                    <tr>
                        <td>${slot.slotID}</td>
                        <td>${slot.slotName}</td>
                        <td>
                            <a href="gear-slot-detail?id=${slot.slotID}" class="btn btn-info btn-sm">View</a>
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