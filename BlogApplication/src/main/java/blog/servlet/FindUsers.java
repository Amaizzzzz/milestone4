package blog.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import blog.dal.BlogUsersDao;
import blog.dal.ConnectionManager;


/**
 * FindUsers is the primary entry point into the application.
 */
@WebServlet("/findusers")
public class FindUsers extends HttpServlet {
  private static final long serialVersionUID = 1L;
  // Message label for response messages
  private static final String RESPONSE_MESSAGE = "response";

  @Override
  public void doGet(
    HttpServletRequest req,
    HttpServletResponse resp
  ) throws ServletException, IOException {
    handleRequest(req, resp);
  }

  @Override
  public void doPost(
    HttpServletRequest req,
    HttpServletResponse resp
  ) throws ServletException, IOException {
    handleRequest(req, resp);
  }

  private void handleRequest(
    HttpServletRequest req,
    HttpServletResponse resp
  ) throws ServletException, IOException {
    // Map for storing messages
    Map<String, String> messages = new HashMap<>();
    req.setAttribute("messages", messages);

    // Retrieve and validate name.  This comes from the query string
    // (for GET queries) or the HTTP headers (for POST queries), but
    // the Servlet interface provides the same API for accessing the
    // value in both cases.
    String firstName = req.getParameter("firstname");
    if (firstName == null || firstName.trim().isEmpty()) {
      messages.put(RESPONSE_MESSAGE, "Please enter a valid name.");
    } else {
      // Retrieve the BlogUsers records and store as an attribute.
      try (Connection cxn = ConnectionManager.getConnection()) {
        req.setAttribute(
          "blogUsers",
          BlogUsersDao.getBlogUsersFromFirstName(cxn, firstName)
        );
        messages.put(RESPONSE_MESSAGE, "Displaying results for " + firstName);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
    }
    req.getRequestDispatcher("/FindUsers.jsp").forward(req, resp);
  }
}
