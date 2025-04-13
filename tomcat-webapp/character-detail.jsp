<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Character Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Character Details</h1>
        
        <div class="card mb-4">
            <div class="card-header">
                <h2>${character.firstName} ${character.lastName}</h2>
            </div>
            <div class="card-body">
                <p><strong>Character ID:</strong> ${character.characterID}</p>
                <p><strong>Player ID:</strong> ${character.playerID}</p>
                <p><strong>Race ID:</strong> ${character.raceID}</p>
                <p><strong>Creation Time:</strong> ${character.creationTime}</p>
                <p><strong>New Player:</strong> ${character.newPlayer ? 'Yes' : 'No'}</p>
                <p><strong>Current Job ID:</strong> ${character.currentJobID}</p>
            </div>
        </div>
        
        <div class="card mb-4">
            <div class="card-header">
                <h3>Stats</h3>
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Stat ID</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="stat" items="${stats}">
                            <tr>
                                <td>${stat.statID}</td>
                                <td>${stat.value}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        
        <div class="card mb-4">
            <div class="card-header">
                <h3>Jobs</h3>
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Job ID</th>
                            <th>Unlocked</th>
                            <th>XP</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="job" items="${jobs}">
                            <tr>
                                <td>${job.jobID}</td>
                                <td>${job.unlocked ? 'Yes' : 'No'}</td>
                                <td>${job.XP}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        
        <div class="card mb-4">
            <div class="card-header">
                <h3>Gear</h3>
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Instance ID</th>
                            <th>Gear Slot</th>
                            <th>Item ID</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="gearInstance" items="${gear}">
                            <tr>
                                <td>${gearInstance.gearInstanceID}</td>
                                <td>${gearInstance.gearSlot.slotName}</td>
                                <td>${gearInstance.gear.itemId}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        
        <a href="characters" class="btn btn-primary">Back to Characters</a>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 