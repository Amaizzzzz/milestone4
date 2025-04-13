<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Characters</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Characters</h1>
        
        <form action="characters" method="get" class="mb-4">
            <div class="input-group">
                <input type="text" name="searchName" class="form-control" placeholder="Search by name...">
                <button type="submit" class="btn btn-primary">Search</button>
            </div>
        </form>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Race ID</th>
                    <th>Creation Time</th>
                    <th>New Player</th>
                    <th>Current Job ID</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="character" items="${characters}">
                    <tr>
                        <td>${character.characterID}</td>
                        <td>${character.firstName} ${character.lastName}</td>
                        <td>${character.raceID}</td>
                        <td>${character.creationTime}</td>
                        <td>${character.newPlayer ? 'Yes' : 'No'}</td>
                        <td>${character.currentJobID}</td>
                        <td>
                            <a href="character?id=${character.characterID}" class="btn btn-sm btn-info">View</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 