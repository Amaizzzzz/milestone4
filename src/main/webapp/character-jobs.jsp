<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Character Jobs</title>
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
        .progress {
            height: 25px;
        }
        .progress-bar {
            line-height: 25px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="mt-4">${character.firstName} ${character.lastName}'s Jobs</h1>
        
        <!-- Filter Section -->
        <div class="filter-section mb-4">
            <form action="character-jobs" method="get" class="form-inline">
                <input type="hidden" name="characterId" value="${param.characterId}">
                <div class="form-group mr-3">
                    <label for="jobName" class="mr-2">Job Name:</label>
                    <input type="text" class="form-control" id="jobName" name="jobName" 
                           value="${param.jobName}" placeholder="Search job name">
                </div>
                <div class="form-group mr-3">
                    <label for="minXP" class="mr-2">Min XP:</label>
                    <input type="number" class="form-control" id="minXP" name="minXP" 
                           value="${param.minXP}" placeholder="Min XP">
                </div>
                <div class="form-group mr-3">
                    <label for="maxXP" class="mr-2">Max XP:</label>
                    <input type="number" class="form-control" id="maxXP" name="maxXP" 
                           value="${param.maxXP}" placeholder="Max XP">
                </div>
                <button type="submit" class="btn btn-primary">Apply Filters</button>
            </form>
        </div>

        <!-- Jobs List -->
        <div class="card">
            <div class="card-header">
                <h5>Job Progression</h5>
            </div>
            <div class="card-body">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Job Name</th>
                            <th>Level</th>
                            <th>XP Progress</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${characterJobs}" var="job">
                            <tr>
                                <td>${job.jobName}</td>
                                <td>${job.level}</td>
                                <td>
                                    <div class="progress">
                                        <div class="progress-bar" role="progressbar" 
                                             style="width: ${job.xpPercentage}%">
                                            ${job.xp}/${job.nextLevelXP} XP
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge badge-${job.isUnlocked ? 'success' : 'secondary'}">
                                        ${job.isUnlocked ? 'Unlocked' : 'Locked'}
                                    </span>
                                    <c:if test="${job.isCurrentJob}">
                                        <span class="badge badge-primary">Current Job</span>
                                    </c:if>
                                </td>
                                <td>
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-warning btn-sm" 
                                                onclick="updateXP(${job.jobID}, ${job.xp})">
                                            Update XP
                                        </button>
                                        <c:if test="${!job.isUnlocked}">
                                            <button type="button" class="btn btn-success btn-sm" 
                                                    onclick="unlockJob(${job.jobID})">
                                                Unlock
                                            </button>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        
        <a href="characters" class="btn btn-secondary mt-3">Back to Characters</a>
    </div>

    <script>
    function updateXP(jobId, currentXP) {
        const newXP = prompt("Enter new XP value:", currentXP);
        if (newXP !== null) {
            window.location.href = `character-jobs?action=updateXP&characterId=${${param.characterId}}&jobId=${jobId}&xp=${newXP}`;
        }
    }
    
    function unlockJob(jobId) {
        if (confirm("Are you sure you want to unlock this job?")) {
            window.location.href = `character-jobs?action=unlock&characterId=${${param.characterId}}&jobId=${jobId}`;
        }
    }
    </script>
</body>
</html> 