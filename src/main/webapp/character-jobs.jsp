<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${character.firstName} ${character.lastName} - Jobs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Jobs for ${character.firstName} ${character.lastName}</h2>
        
        <!-- Filtering Form -->
        <form method="get" action="character-jobs" class="mb-4">
            <input type="hidden" name="characterId" value="${character.characterId}">
            <div class="row">
                <div class="col-md-3">
                    <input type="text" name="jobNameFilter" class="form-control" placeholder="Job Name" 
                           value="${param.jobNameFilter}">
                </div>
                <div class="col-md-3">
                    <input type="number" name="minXp" class="form-control" placeholder="Min XP" 
                           value="${param.minXp}">
                </div>
                <div class="col-md-3">
                    <input type="number" name="maxXp" class="form-control" placeholder="Max XP" 
                           value="${param.maxXp}">
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-primary">Apply Filters</button>
                </div>
            </div>
        </form>

        <!-- Jobs Table -->
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Job Name</th>
                    <th>Current XP</th>
                    <th>Level</th>
                    <th>Current Job</th>
                    <th>Weapon</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${characterJobs}" var="job">
                    <tr>
                        <td>${job.jobName}</td>
                        <td>${job.xp}</td>
                        <td>${job.level}</td>
                        <td>${job.isCurrentJob ? 'Yes' : 'No'}</td>
                        <td>${job.weaponName}</td>
                        <td>
                            <button type="button" class="btn btn-warning btn-sm" 
                                    data-bs-toggle="modal" 
                                    data-bs-target="#updateXpModal${job.jobId}">
                                Update XP
                            </button>
                            <c:if test="${!job.isCurrentJob}">
                                <form action="character-jobs/set-current" method="post" style="display: inline;">
                                    <input type="hidden" name="characterId" value="${character.characterId}">
                                    <input type="hidden" name="jobId" value="${job.jobId}">
                                    <button type="submit" class="btn btn-success btn-sm">Set as Current</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                    
                    <!-- Update XP Modal -->
                    <div class="modal fade" id="updateXpModal${job.jobId}" tabindex="-1">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">Update XP for ${job.jobName}</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <form action="character-jobs/update-xp" method="post">
                                    <div class="modal-body">
                                        <input type="hidden" name="characterId" value="${character.characterId}">
                                        <input type="hidden" name="jobId" value="${job.jobId}">
                                        <div class="mb-3">
                                            <label for="xp${job.jobId}" class="form-label">XP Amount</label>
                                            <input type="number" class="form-control" id="xp${job.jobId}" 
                                                   name="xp" value="${job.xp}" required>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        <button type="submit" class="btn btn-primary">Save changes</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </tbody>
        </table>
        
        <a href="characters" class="btn btn-secondary">Back to Characters</a>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 