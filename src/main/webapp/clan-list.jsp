<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clans List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Clans</h1>
        <a href="create-clan" class="btn btn-primary mb-3">Create New Clan</a>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Clan ID</th>
                    <th>Clan Name</th>
                    <th>Leader ID</th>
                    <th>Creation Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${clans}" var="clan">
                    <tr>
                        <td>${clan.clanID}</td>
                        <td>${clan.clanName}</td>
                        <td>${clan.leaderID}</td>
                        <td>${clan.creationDate}</td>
                        <td>
                            <a href="clan?id=${clan.clanID}" class="btn btn-info btn-sm">View</a>
                            <a href="edit-clan?id=${clan.clanID}" class="btn btn-warning btn-sm">Edit</a>
                            <a href="delete-clan?id=${clan.clanID}" class="btn btn-danger btn-sm" 
                               onclick="return confirm('Are you sure you want to delete this clan?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 