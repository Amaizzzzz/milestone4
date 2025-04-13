package blog.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import blog.dal.BlogPostsDao;
import blog.dal.BlogUsersDao;
import blog.dal.ConnectionManager;
import blog.model.BlogPosts;
import blog.model.BlogUsers;


@WebServlet("/userblogposts")
public class UserBlogPosts extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final String TITLE_MESSAGE = "title";

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<>();
    req.setAttribute("messages", messages);

    List<BlogPosts> blogPosts = new ArrayList<>();

    // Retrieve and validate UserName.
    String userName = req.getParameter("username");
    if (userName == null || userName.trim().isEmpty()) {
      messages.put(TITLE_MESSAGE, "Invalid username.");
    } else {
      messages.put(TITLE_MESSAGE, "BlogPosts for " + userName);
    }

    // Retrieve BlogUsers, and store in the request.
    try (Connection cxn = ConnectionManager.getConnection()) {
      BlogUsers user = BlogUsersDao.getBlogUserFromUserName(cxn, userName);
      if (user == null) {
        messages.put(TITLE_MESSAGE, "Username " + userName + " not found");
      } else {
        blogPosts = BlogPostsDao.getBlogPostsForUser(cxn, user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IOException(e);
    }
    req.setAttribute("blogPosts", blogPosts);
    req.getRequestDispatcher("/UserBlogPosts.jsp").forward(req, resp);
  }
}
