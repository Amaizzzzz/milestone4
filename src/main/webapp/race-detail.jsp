<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Race Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Race Details</h1>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${race.raceName}</h5>
                <p class="card-text">
                    <strong>Race ID:</strong> ${race.raceID}<br>
                    <strong>Description:</strong> ${race.description}<br>
                    <strong>Base Stats:</strong><br>
                    • Strength: ${race.baseStrength}<br>
                    • Dexterity: ${race.baseDexterity}<br>
                    • Intelligence: ${race.baseIntelligence}<br>
                </p>
            </div>
        </div>

        <h2 class="mt-4">Characters of this Race</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Character ID</th>
                    <th>Character Name</th>
                    <th>Current Job</th>
                    <th>Creation Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${characters}" var="character">
                    <tr>
                        <td>${character.characterID}</td>
                        <td>${character.firstName} ${character.lastName}</td>
                        <td>${character.currentJob}</td>
                        <td>${character.creationDate}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <h2 class="mt-4">Racial Bonuses</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Stat Type</th>
                    <th>Bonus Value</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${racialBonuses}" var="bonus">
                    <tr>
                        <td>${bonus.statType}</td>
                        <td>${bonus.bonusValue}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="mt-3">
            <a href="races" class="btn btn-secondary">Back to Races</a>
            <a href="edit-race?id=${race.raceID}" class="btn btn-warning">Edit Race</a>
            <a href="delete-race?id=${race.raceID}" class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this race?')">Delete Race</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 