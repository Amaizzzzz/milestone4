<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="cs5200project.dal.ConnectionManager" %>
<%@ page import="cs5200project.dal.PlayerDao" %>
<%@ page import="cs5200project.model.Player" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Players</title>
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
        .error {
            color: red;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid red;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="mt-4">Players</h1>
        
        <!-- Display messages -->
        <div class="alert alert-info">
            ${messages.response}
        </div>
        
        <!-- Create Player Form -->
        <div class="card mb-4">
            <div class="card-header">
                <h5>Create New Player</h5>
            </div>
            <div class="card-body">
                <form action="players" method="post">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label for="serverRegion">Server Region:</label>
                        <input type="text" class="form-control" id="serverRegion" name="serverRegion" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Create Player</button>
                </form>
            </div>
        </div>

        <!-- Players List -->
        <div class="card">
            <div class="card-header">
                <h5>All Players</h5>
            </div>
            <div class="card-body">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Server Region</th>
                            <th>Created At</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            Connection cxn = null;
                            try {
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                cxn = ConnectionManager.getInstance().getConnection();
                                List<Player> players = PlayerDao.getInstance().getAllPlayers();
                                if (players != null && !players.isEmpty()) {
                                    for (Player player : players) {
                        %>
                        <tr>
                            <td><%= player.getPlayerID() %></td>
                            <td><%= player.getUsername() %></td>
                            <td><%= player.getEmail() %></td>
                            <td><%= player.getServerRegion() %></td>
                            <td><%= player.getCreatedAt() %></td>
                        </tr>
                        <%
                                    }
                                } else {
                        %>
                        <tr>
                            <td colspan="4" class="error">No players found in the database.</td>
                        </tr>
                        <%
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                out.println("<tr><td colspan='4' class='error'>Database error: " + e.getMessage() + "</td></tr>");
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                out.println("<tr><td colspan='4' class='error'>MySQL JDBC Driver not found: " + e.getMessage() + "</td></tr>");
                            } catch (Exception e) {
                                e.printStackTrace();
                                out.println("<tr><td colspan='4' class='error'>Error loading players: " + e.getMessage() + "</td></tr>");
                            } finally {
                                if (cxn != null) {
                                    try {
                                        cxn.close();
                                    } catch (Exception e) {
                                        // Ignore
                                    }
                                }
                            }
                        %>
        <h1>Players List</h1>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Server Region</th>
                </tr>
            </thead>
            <tbody>
                <%
                    Connection cxn = null;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        cxn = ConnectionManager.getInstance().getConnection();
                        List<Player> players = PlayerDao.getInstance().getAllPlayers();
                        if (players != null && !players.isEmpty()) {
                            for (Player player : players) {
                %>
                <tr>
                    <td><%= player.getPlayerID() %></td>
                    <td><%= player.getUsername() %></td>
                    <td><%= player.getEmail() %></td>
                    <td><%= player.getServerRegion() %></td>
                </tr>
                <%
                            }
                        } else {
                %>
                <tr>
                    <td colspan="4" class="error">No players found in the database.</td>
                </tr>
                <%
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        out.println("<tr><td colspan='4' class='error'>Database error: " + e.getMessage() + "</td></tr>");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        out.println("<tr><td colspan='4' class='error'>MySQL JDBC Driver not found: " + e.getMessage() + "</td></tr>");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.println("<tr><td colspan='4' class='error'>Error loading players: " + e.getMessage() + "</td></tr>");
                    } finally {
                        if (cxn != null) {
                            try {
                                cxn.close();
                            } catch (Exception e) {
                                // Ignore
                            }
                        }
                    }
                %>
            </tbody>
        </table>
        <a href="index.jsp" class="back-link">Back to Home</a>
    </div>
</body>
</html> 