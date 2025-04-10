<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Weapon Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Weapon Details</h1>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${weapon.itemName}</h5>
                <p class="card-text">
                    <strong>Item ID:</strong> ${weapon.itemID}<br>
                    <strong>Item Level:</strong> ${weapon.itemLevel}<br>
                    <strong>Required Level:</strong> ${weapon.requiredLevel}<br>
                    <strong>Weapon Type:</strong> ${weapon.weaponType}<br>
                    <strong>Damage:</strong> ${weapon.damage}<br>
                    <strong>Attack Speed:</strong> ${weapon.attackSpeed}<br>
                    <strong>DPS:</strong> ${weapon.damage * weapon.attackSpeed}<br>
                    <strong>Max Stack Size:</strong> ${weapon.maxStackSize}<br>
                    <strong>Price:</strong> ${weapon.price}<br>
                    <strong>Durability:</strong> ${weapon.weaponDurability}<br>
                    <strong>Rank:</strong> ${weapon.rankValue}<br>
                </p>
            </div>
        </div>

        <h2 class="mt-4">Stat Bonuses</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Stat Type</th>
                    <th>Bonus Value</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${weaponStats}" var="stat">
                    <tr>
                        <td>${stat.statType}</td>
                        <td>${stat.bonusValue}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <h2 class="mt-4">Required Jobs</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Job ID</th>
                    <th>Job Name</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${requiredJobs}" var="job">
                    <tr>
                        <td>${job.jobID}</td>
                        <td>${job.jobName}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="mt-3">
            <a href="weapons" class="btn btn-secondary">Back to Weapons</a>
            <a href="edit-weapon?id=${weapon.itemID}" class="btn btn-warning">Edit Weapon</a>
            <a href="delete-weapon?id=${weapon.itemID}" class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this weapon?')">Delete Weapon</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 