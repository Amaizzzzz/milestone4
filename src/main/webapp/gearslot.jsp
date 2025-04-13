<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gear Slots</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Gear Slots</h1>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${gearSlots}" var="slot">
                    <tr>
                        <td>${slot.gearSlotID}</td>
                        <td>${slot.slotName}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="index.jsp" class="btn btn-primary">Back to Home</a>
    </div>
    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html> 