<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Characters with Selected Job</title>
  </head>
  <body>
    <h1>Characters with This Job</h1>

    <p><b>${messages.response}</b></p>

    <c:if test="${not empty charactersWithJob}">
      <table border="1">
        <tr>
          <th>Character Name</th>
          <th>Job Name</th>
          <th>Current XP</th>
          <th>Is Current Job</th>
          <th>Held Weapon</th>
        </tr>

        <c:forEach var="entry" items="${charactersWithJob}">
          <tr>
            <td>${entry.characterName}</td>
            <td>${entry.jobName}</td>
            <td>${entry.xp}</td>
            <td><c:out value="${entry.isCurrent}" /></td>
            <td><c:out value="${entry.weaponName}" default="None" /></td>
          </tr>
        </c:forEach>
      </table>
    </c:if>

    <br/>
    <a href="findcharacters">Back to Character List</a>
  </body>
</html>
