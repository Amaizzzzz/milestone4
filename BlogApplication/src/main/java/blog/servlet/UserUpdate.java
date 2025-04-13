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
import blog.model.BlogUsers;


@WebServlet("/userupdate")
public class UserUpdate extends HttpServlet {
  private static final long serialVersionUID = 1L;
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
    // Map for storing messages.
    Map<String, String> messages = new HashMap<>();
    req.setAttribute("messages", messages);

    // Retrieve user and validate.
    String userName = req.getParameter("username");
    if (userName == null || userName.trim().isEmpty()) {
      messages.put(RESPONSE_MESSAGE, "Please enter a valid UserName.");
    } else {
      try (Connection cxn = ConnectionManager.getConnection()) {
        BlogUsers blogUser =
          BlogUsersDao.getBlogUserFromUserName(cxn, userName);
        if (blogUser == null) {
          messages.put(
            RESPONSE_MESSAGE,
            "UserName does not exist. No update to perform."
          );
        } else {
          String newLastName = req.getParameter("lastname");
          if (newLastName == null || newLastName.trim().isEmpty()) {
            messages.put(RESPONSE_MESSAGE, "Please enter a valid LastName.");
          } else {
            BlogUsersDao.updateLastName(cxn, blogUser, newLastName);
            messages.put(RESPONSE_MESSAGE, "Successfully updated " + userName);
          }
        }
        req.setAttribute("blogUser", blogUser);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
    }

    req.getRequestDispatcher("/UserUpdate.jsp").forward(req, resp);
  }
}
