<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Character Details</title>
    <style>
        .section {
            margin: 20px 0;
            padding: 10px;
            border: 1px solid #ddd;
        }
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .edit-button {
            margin: 20px 0;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        .edit-button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <h1>Character Details</h1>
    
    <div class="section">
        <h2>Basic Information</h2>
        <table>
            <tr>
                <th>Name:</th>
                <td>${character.firstName} ${character.lastName}</td>
            </tr>
            <tr>
                <th>Player:</th>
                <td>${character.playerName}</td>
            </tr>
            <tr>
                <th>Race:</th>
                <td>${character.raceName}</td>
            </tr>
            <tr>
                <th>Creation Date:</th>
                <td>${character.creationDate}</td>
            </tr>
            <tr>
                <th>Current Job:</th>
                <td>${character.currentJobName}</td>
            </tr>
        </table>
    </div>

    <div class="section">
        <h2>Character Stats</h2>
        <table>
            <thead>
                <tr>
                    <th>Stat Name</th>
                    <th>Base Value</th>
                    <th>Current Value</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${characterStats}" var="stat">
                    <tr>
                        <td>${stat.statName}</td>
                        <td>${stat.baseValue}</td>
                        <td>${stat.currentValue}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="section">
        <h2>Jobs</h2>
        <table>
            <thead>
                <tr>
                    <th>Job Name</th>
                    <th>Level</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${characterJobs}" var="job">
                    <tr>
                        <td>${job.jobName}</td>
                        <td>${job.jobLevel}</td>
                        <td>${job.isUnlocked ? 'Unlocked' : 'Locked'}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="section">
        <h2>Equipment</h2>
        <table>
            <thead>
                <tr>
                    <th>Slot</th>
                    <th>Item Name</th>
                    <th>Item Level</th>
                    <th>Required Level</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${equipment}" var="gear">
                    <tr>
                        <td>${gear.slotName}</td>
                        <td>${gear.itemName}</td>
                        <td>${gear.itemLevel}</td>
                        <td>${gear.requiredLevel}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <a href="character/edit?id=${character.characterID}" class="edit-button">Edit Character</a>
    <a href="characters" style="margin-left: 10px;">Back to Character List</a>
</body>
</html> 