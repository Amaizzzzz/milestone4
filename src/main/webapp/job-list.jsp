<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Jobs List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Game Jobs</h1>
        <a href="create-job" class="btn btn-primary mb-3">Create New Job</a>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Job ID</th>
                    <th>Job Name</th>
                    <th>Description</th>
                    <th>Base HP</th>
                    <th>Base MP</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${jobs}" var="job">
                    <tr>
                        <td>${job.jobID}</td>
                        <td>${job.jobName}</td>
                        <td>${job.description}</td>
                        <td>${job.baseHP}</td>
                        <td>${job.baseMP}</td>
                        <td>
                            <a href="job?id=${job.jobID}" class="btn btn-info btn-sm">View</a>
                            <a href="edit-job?id=${job.jobID}" class="btn btn-warning btn-sm">Edit</a>
                            <a href="delete-job?id=${job.jobID}" class="btn btn-danger btn-sm"
                               onclick="return confirm('Are you sure you want to delete this job?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 