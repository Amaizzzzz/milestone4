<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Character Statistics</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .message {
            margin: 10px 0;
            padding: 10px;
            border-radius: 4px;
        }
        .success {
            background-color: #dff0d8;
            color: #3c763d;
        }
        .error {
            background-color: #f2dede;
            color: #a94442;
        }
    </style>
</head>
<body>
    <h1>Character Statistics</h1>

    <c:if test="${not empty messages.error}">
        <div class="message error">
            ${messages.error}
        </div>
    </c:if>
    <c:if test="${not empty messages.success}">
        <div class="message success">
            ${messages.success}
        </div>
    </c:if>

    <c:if test="${not empty characterStats}">
        <table>
            <tr>
                <th>Stat Type</th>
                <th>Value</th>
                <th>Description</th>
            </tr>
            <c:forEach items="${characterStats}" var="stat">
                <tr>
                    <td>${stat.statType.name}</td>
                    <td>${stat.value}</td>
                    <td>${stat.statType.description}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <br/>
    <a href="findcharacters">Back to Character List</a>
</body>
</html> 