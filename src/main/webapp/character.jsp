<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="cs5200project.model.GameCharacter" %>
<%@ page import="cs5200project.dal.CharacterDao" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Characters</title>
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
        .filter-section {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        h1 {
            color: #333;
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            color: #0066cc;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="mt-4">Characters</h1>
        
        <!-- Filter and Sort Section -->
        <div class="filter-section">
            <form action="characters" method="get" class="form-inline">
                <div class="form-group mr-3">
                    <label for="nameSearch" class="mr-2">Search Name:</label>
                    <input type="text" class="form-control" id="nameSearch" name="nameSearch" 
                           value="${param.nameSearch}" placeholder="Enter character name">
                </div>
                <div class="form-group mr-3">
                    <label for="raceId" class="mr-2">Race:</label>
                    <select class="form-control" id="raceId" name="raceId">
                        <option value="">All Races</option>
                        <c:forEach items="${races}" var="race">
                            <option value="${race.raceID}" 
                                    ${param.raceId == race.raceID ? 'selected' : ''}>
                                ${race.raceName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group mr-3">
                    <label for="sortBy" class="mr-2">Sort By:</label>
                    <select class="form-control" id="sortBy" name="sortBy">
                        <option value="">Default</option>
                        <option value="name" ${param.sortBy == 'name' ? 'selected' : ''}>Name</option>
                        <option value="creation" ${param.sortBy == 'creation' ? 'selected' : ''}>Creation Time</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Apply Filters</button>
            </form>
        </div>

        <!-- Characters List -->
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">All Characters</h5>
                <a href="characters?action=create" class="btn btn-success">Create New Character</a>
            </div>
            <div class="card-body">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Race</th>
                            <th>Current Job</th>
                            <th>New Player</th>
                            <th>Created At</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${characters}" var="character">
                            <tr>
                                <td>${character.characterID}</td>
                                <td>${character.firstName} ${character.lastName}</td>
                                <td>${character.raceName}</td>
                                <td>${character.jobName}</td>
                                <td>${character.isNewPlayer ? 'Yes' : 'No'}</td>
                                <td>${character.creationDate}</td>
                                <td>
                                    <div class="btn-group">
                                        <a href="character-jobs?characterId=${character.characterID}" 
                                           class="btn btn-info btn-sm">Jobs</a>
                                        <a href="character-stats?characterId=${character.characterID}" 
                                           class="btn btn-info btn-sm">Stats</a>
                                        <a href="characters?action=update&id=${character.characterID}" 
                                           class="btn btn-warning btn-sm">Update</a>
                                        <a href="characters?action=delete&id=${character.characterID}" 
                                           class="btn btn-danger btn-sm" 
                                           onclick="return confirm('Are you sure you want to delete this character?')">Delete</a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html> 