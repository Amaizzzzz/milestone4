<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="cs5200project.model.GameCharacter" %>
<%@ page import="cs5200project.dal.CharacterDao" %>
<%@ page import="java.util.List" %>
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
            max-width: 800px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
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
        
        <!-- Display messages -->
        <div class="alert alert-info">
            ${messages.response}
        </div>
        
        <!-- Create Character Form -->
        <div class="card mb-4">
            <div class="card-header">
                <h5>Create New Character</h5>
            </div>
            <div class="card-body">
                <form action="characters" method="post">
                    <div class="form-group">
                        <label for="playerID">Player ID:</label>
                        <input type="number" class="form-control" id="playerID" name="playerID" required>
                    </div>
                    <div class="form-group">
                        <label for="firstName">First Name:</label>
                        <input type="text" class="form-control" id="firstName" name="firstName" required>
                    </div>
                    <div class="form-group">
                        <label for="lastName">Last Name:</label>
                        <input type="text" class="form-control" id="lastName" name="lastName" required>
                    </div>
                    <div class="form-group">
                        <label for="raceID">Race ID:</label>
                        <input type="number" class="form-control" id="raceID" name="raceID" required>
                    </div>
                    <div class="form-group">
                        <label for="currentJobID">Current Job ID:</label>
                        <input type="number" class="form-control" id="currentJobID" name="currentJobID" required>
                    </div>
                    <div class="form-group">
                        <label for="isNewPlayer">Is New Player:</label>
                        <select class="form-control" id="isNewPlayer" name="isNewPlayer">
                            <option value="true">Yes</option>
                            <option value="false">No</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Create Character</button>
                </form>
            </div>
        </div>

        <!-- Characters List -->
        <div class="card">
            <div class="card-header">
                <h5>All Characters</h5>
            </div>
            <div class="card-body">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Player ID</th>
                            <th>Name</th>
                            <th>Race ID</th>
                            <th>Job ID</th>
                            <th>New Player</th>
                            <th>Created At</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            try {
                                List<GameCharacter> characters = (List<GameCharacter>) request.getAttribute("characters");
                                if (characters == null) {
                                    // If not set in request, get it directly from DAO
                                    CharacterDao characterDao = CharacterDao.getInstance();
                                    characters = characterDao.getAllCharacters();
                                }
                                if (characters != null && !characters.isEmpty()) {
                                    for (GameCharacter character : characters) {
                        %>
                        <tr>
                            <td><%= character.getCharacterID() %></td>
                            <td><%= character.getPlayerID() %></td>
                            <td><%= character.getFirstName() %> <%= character.getLastName() %></td>
                            <td><%= character.getRaceID() %></td>
                            <td><%= character.getCurrentJobID() %></td>
                            <td><%= character.isNewPlayer() ? "Yes" : "No" %></td>
                            <td><%= character.getCreationDate() %></td>
                        </tr>
                        <%
                                    }
                                } else {
                        %>
                        <tr>
                            <td colspan="7">No characters found</td>
                        </tr>
                        <%
                                }
                            } catch (Exception e) {
                                out.println("<tr><td colspan='7'>Error loading characters: " + e.getMessage() + "</td></tr>");
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html> 