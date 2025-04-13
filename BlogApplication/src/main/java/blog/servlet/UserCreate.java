package blog.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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


@WebServlet("/usercreate")
public class UserCreate extends HttpServlet {
  private static final long serialVersionUID = 1L;

  // Message label for response messages
  private static final String RESPONSE_MESSAGE = "response";

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    req.setAttribute("messages", new HashMap<>());
    //Just render the JSP.
    req.getRequestDispatcher("/UserCreate.jsp").forward(req, resp);
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
      messages.put(RESPONSE_MESSAGE, "Invalid UserName");
    } else {
      // Create the BlogUser.
      String firstName = req.getParameter("firstname");
      String lastName = req.getParameter("lastname");
      // dob must be in the format yyyy-mm-dd.
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String stringDob = req.getParameter("dob");
      Date dob = new Date();
      try {
        dob = dateFormat.parse(stringDob);
      } catch (ParseException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
      try (Connection cxn = ConnectionManager.getConnection()) {
        // Exercise: parse the input for StatusLevel.
        BlogUsersDao.create(
          cxn,
          userName,
          firstName,
          lastName,
          dob,
          BlogUsers.StatusLevel.novice
        );
        messages.put(RESPONSE_MESSAGE, "Successfully created " + userName);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
    }

    req.getRequestDispatcher("/UserCreate.jsp").forward(req, resp);
  }
}
