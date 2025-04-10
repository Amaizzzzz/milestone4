<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clan Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Clan Details</h1>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${clan.clanName}</h5>
                <p class="card-text">
                    <strong>Clan ID:</strong> ${clan.clanID}<br>
                    <strong>Leader ID:</strong> ${clan.leaderID}<br>
                    <strong>Creation Date:</strong> ${clan.creationDate}<br>
                </p>
            </div>
        </div>

        <h2 class="mt-4">Clan Members</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Character ID</th>
                    <th>Character Name</th>
                    <th>Role</th>
                    <th>Join Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${clanMembers}" var="member">
                    <tr>
                        <td>${member.characterID}</td>
                        <td>${member.characterName}</td>
                        <td>${member.role}</td>
                        <td>${member.joinDate}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="mt-3">
            <a href="clans" class="btn btn-secondary">Back to Clans</a>
            <a href="edit-clan?id=${clan.clanID}" class="btn btn-warning">Edit Clan</a>
            <a href="delete-clan?id=${clan.clanID}" class="btn btn-danger" 
               onclick="return confirm('Are you sure you want to delete this clan?')">Delete Clan</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 