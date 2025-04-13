<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Character Stats</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f0f0;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .stat-card {
            margin-bottom: 20px;
        }
        .bonus-value {
            color: #28a745;
        }
        .total-value {
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="mt-4">${character.firstName} ${character.lastName}'s Statistics</h1>
        
        <!-- Stats List -->
        <div class="row">
            <c:forEach items="${characterStats}" var="stat">
                <div class="col-md-6">
                    <div class="card stat-card">
                        <div class="card-header">
                            <h5 class="mb-0">${stat.statType}</h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Base Value:</strong> ${stat.baseValue}</p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Equipment Bonus:</strong> 
                                        <span class="bonus-value">+${stat.equipmentBonus}</span>
                                    </p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <p class="total-value">Total: ${stat.baseValue + stat.equipmentBonus}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <button type="button" class="btn btn-warning btn-sm" 
                                            onclick="updateStat(${stat.statID}, ${stat.baseValue})">
                                        Update Base Value
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <a href="characters" class="btn btn-secondary mt-3">Back to Characters</a>
    </div>

    <script>
    function updateStat(statId, currentValue) {
        const newValue = prompt("Enter new base value:", currentValue);
        if (newValue !== null) {
            window.location.href = `character-stats?action=update&characterId=${${param.characterId}}&statId=${statId}&value=${newValue}`;
        }
    }
    </script>
</body>
</html> 