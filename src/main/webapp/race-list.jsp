<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Races List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Game Races</h1>
        <a href="create-race" class="btn btn-primary mb-3">Create New Race</a>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Race ID</th>
                    <th>Race Name</th>
                    <th>Description</th>
                    <th>Base Strength</th>
                    <th>Base Dexterity</th>
                    <th>Base Intelligence</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${races}" var="race">
                    <tr>
                        <td>${race.raceID}</td>
                        <td>${race.raceName}</td>
                        <td>${race.description}</td>
                        <td>${race.baseStrength}</td>
                        <td>${race.baseDexterity}</td>
                        <td>${race.baseIntelligence}</td>
                        <td>
                            <a href="race?id=${race.raceID}" class="btn btn-info btn-sm">View</a>
                            <a href="edit-race?id=${race.raceID}" class="btn btn-warning btn-sm">Edit</a>
                            <a href="delete-race?id=${race.raceID}" class="btn btn-danger btn-sm"
                               onclick="return confirm('Are you sure you want to delete this race?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 