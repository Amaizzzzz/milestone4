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

import blog.dal.BlogCommentsDao;
import blog.dal.BlogPostsDao;
import blog.dal.BlogUsersDao;
import blog.dal.ConnectionManager;
import blog.model.BlogComments;
import blog.model.BlogPosts;
import blog.model.BlogUsers;


@WebServlet("/blogcomments")
public class UserPostBlogComments extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final String TITLE_MESSAGE = "title";

  @Override
  public void doGet(
    HttpServletRequest req,
    HttpServletResponse resp
  ) throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<>();
    req.setAttribute("messages", messages);

    List<BlogComments> blogComments = new ArrayList<>();

    // Retrieve BlogComments depending on valid PostId or UserName.
    String postId = req.getParameter("postid");
    String userName = req.getParameter("username");

    try (Connection cxn = ConnectionManager.getConnection()) {
      if (postId != null && !postId.trim().isEmpty()) {
        // If the postid param is provided then ignore the username param.

        BlogPosts post = BlogPostsDao.getBlogPostById(
          cxn,
          Integer.parseInt(postId)
        );

        if (post == null) {
          messages.put(TITLE_MESSAGE, "postId " + postId + " not found");
        } else {
          blogComments = BlogCommentsDao.getBlogCommentsForPost(cxn, post);
          messages.put(TITLE_MESSAGE, "BlogComments for PostId " + postId);
        }
      } else if (userName != null && !userName.trim().isEmpty()) {
        // If postid is invalid, then use the username param.

        BlogUsers user = BlogUsersDao.getBlogUserFromUserName(cxn, userName);
        if (user == null) {
          messages.put(TITLE_MESSAGE, "user " + userName + " not found");
        } else {
          blogComments = BlogCommentsDao.getBlogCommentsForUser(cxn, user);
          messages.put("title", "BlogComments for UserName " + userName);
        }
      } else {
        messages.put("title", "Invalid PostId and UserName.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new IOException(e);
    }

    req.setAttribute("blogComments", blogComments);
    req.getRequestDispatcher("/UserPostBlogComments.jsp").forward(req, resp);
  }
}
