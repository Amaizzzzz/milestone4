<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Update Character Name</title>
  </head>
  
  <body>
	<form method="post" action="characterupdate">
	  <input type="hidden" name="characterId" value="${character.characterID}" />
	  <p><label>First Name:</label>
	     <input type="text" name="firstName" value="${character.firstName}" /></p>
	  <p><label>Last Name:</label>
	     <input type="text" name="lastName" value="${character.lastName}" /></p>
	  <p><input type="submit" value="Update" /></p>
	</form>
  </body>
 </html>
 
