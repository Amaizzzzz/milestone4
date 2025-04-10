<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Job Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Job Details</h1>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${job.jobName}</h5>
                <p class="card-text">
                    <strong>Job ID:</strong> ${job.jobID}<br>
                    <strong>Description:</strong> ${job.description}<br>
                    <strong>Base HP:</strong> ${job.baseHP}<br>
                    <strong>Base MP:</strong> ${job.baseMP}<br>
                </p>
            </div>
        </div>

        <h2 class="mt-4">Characters with this Job</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Character ID</th>
                    <th>Character Name</th>
                    <th>Level</th>
                    <th>Race</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${characters}" var="character">
                    <tr>
                        <td>${character.characterID}</td>
                        <td>${character.firstName} ${character.lastName}</td>
                        <td>${character.level}</td>
                        <td>${character.race}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <h2 class="mt-4">Available Gear</h2>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Item ID</th>
                    <th>Item Name</th>
                    <th>Item Level</th>
                    <th>Required Level</th>
                    <th>Gear Slot</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${availableGear}" var="gear">
                    <tr>
                        <td>${gear.itemID}</td>
                        <td>${gear.itemName}</td>
                        <td>${gear.itemLevel}</td>
                        <td>${gear.requiredLevel}</td>
                        <td>${gear.gearSlot}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="mt-3">
            <a href="jobs" class="btn btn-secondary">Back to Jobs</a>
            <a href="edit-job?id=${job.jobID}" class="btn btn-warning">Edit Job</a>
            <a href="delete-job?id=${job.jobID}" class="btn btn-danger"
               onclick="return confirm('Are you sure you want to delete this job?')">Delete Job</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 