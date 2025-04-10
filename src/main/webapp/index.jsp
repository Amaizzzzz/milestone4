<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Game Database</title>
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
        .nav-links {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 20px;
        }
        .nav-links a {
            text-decoration: none;
            color: #0066cc;
            padding: 10px 20px;
            border: 1px solid #0066cc;
            border-radius: 5px;
            transition: all 0.3s ease;
        }
        .nav-links a:hover {
            background-color: #0066cc;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to Game Database</h1>
        <div class="nav-links">
            <a href="player.jsp">View Players</a>
            <a href="character.jsp">View Characters</a>
        </div>
    </div>
</body>
</html> 