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


@WebServlet("/userdelete")
public class UserDelete extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final String TITLE_MESSAGE = "title";
  private static final String DISABLE_SUBMIT_MESSAGE = "disableSubmit";

  @Override
  public void doGet(
    HttpServletRequest req,
    HttpServletResponse resp
  ) throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<>();
    req.setAttribute("messages", messages);
    // Provide a title and render the JSP.
    messages.put(TITLE_MESSAGE, "Delete BlogUser");
    req.getRequestDispatcher("/UserDelete.jsp").forward(req, resp);
  }

  @Override
  public void doPost(
    HttpServletRequest req,
    HttpServletResponse resp
  ) throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<>();
    req.setAttribute("messages", messages);

    // Retrieve and validate name.
    String userName = req.getParameter("username");
    if (userName == null || userName.trim().isEmpty()) {
      messages.put(TITLE_MESSAGE, "Invalid UserName");
      messages.put(DISABLE_SUBMIT_MESSAGE, "true");
    } else {
      // Delete the BlogUser.
      try (Connection cxn = ConnectionManager.getConnection()) {
        int numAffectedRows = BlogUsersDao.deleteByUserName(cxn, userName);
        // Update the message.
        if (numAffectedRows > 0) {
          messages.put(TITLE_MESSAGE, "Successfully deleted " + userName);
          messages.put(DISABLE_SUBMIT_MESSAGE, "true");
        } else {
          messages.put(TITLE_MESSAGE, "Failed to delete " + userName);
          messages.put(DISABLE_SUBMIT_MESSAGE, "false");
        }
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
    }

    req.getRequestDispatcher("/UserDelete.jsp").forward(req, resp);
  }
}
