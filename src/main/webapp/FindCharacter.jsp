<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find Character</title>
  </head>
  <body>
	<form action="findcharacters" method="post">
	  <h1>Search for Characters</h1>
	
	  <p>
	    <label for="searchType">Search by:</label>
	    <select id="searchType" name="searchType">
	      <option value="lastName" ${param.searchType == 'lastName' ? 'selected' : ''}>Last Name</option>
	      <option value="raceName" ${param.searchType == 'raceName' ? 'selected' : ''}>Race Name</option>
	    </select>
	  </p>
	
	  <p>
	    <label for="searchValue">Enter value:</label>
	    <input id="searchValue" name="searchValue" value="${fn:escapeXml(param.searchValue)}" />
	  </p>
	  
		<p>
		  <label for="sortField">Sort by:</label>
		  <select id="sortField" name="sortField">
		    <option value="none" ${param.sortField == 'none' ? 'selected' : ''}>Default</option>
		    <option value="name" ${param.sortField == 'name' ? 'selected' : ''}>Full Name</option>
		    <option value="creation" ${param.sortField == 'creation' ? 'selected' : ''}>Creation Time</option>
			  </select>
		</p>
	  
	  <p>Currently sorted by: ${param.sortField}</p>
	
	  <p>
	    <input type="submit" value="Search">
	    <br/><br/>
	    <span id="responseMessage"><b>${messages.response}</b></span>
	  </p>
	</form>


    <c:if test="${not empty characterList}">
      <h2>Matching Characters</h2>
      <table border="1">
        <tr>
          <th>Character Name</th>
          <th>Race</th>
          <th>Current Job</th>
          <th>Creation Time</th>
          <th>Jobs</th>
          <th>Statistics</th>
          <th>Is New Player</th>
          <th>Update</th>
          <th>Delete</th>
        </tr>
        <c:forEach items="${characterList}" var="character">
          <tr>
            <td>${character.firstName} ${character.lastName}</td>
            <td>${character.race.raceName}</td>
            <td>${character.currentJob.jobName}</td>
            <td><fmt:formatDate value="${character.creationTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><a href="characterjobs?characterId=${character.characterID}">Jobs</a></td>
            <td><a href="characterstats?characterId=${character.characterID}">Statistics</a></td>
            <td><c:out value="${character.newPlayer}" /></td>
            <td><a href="characterupdate?characterId=${character.characterID}">Update Name</a></td>
            <td><a href="characterdelete?characterId=${character.characterID}">Delete</a></td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </body>
</html>
