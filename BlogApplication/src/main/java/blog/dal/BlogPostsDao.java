package blog.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import blog.model.BlogPosts;
import blog.model.BlogUsers;


public class BlogPostsDao {
  private BlogPostsDao() {}

  public static BlogPosts create(
    final Connection cxn,
    final String title,
    final String content,
    final boolean isPublished,
    final Date created,
    final BlogUsers author
  ) throws SQLException {
    final String insertBlogPost = """
      INSERT INTO BlogPosts (Title, Content, Published, Created, UserName)
        VALUES (?, ?, ?, ?, ?);""";
    try (
      PreparedStatement insertStmt = cxn.prepareStatement(
        insertBlogPost,
        Statement.RETURN_GENERATED_KEYS
      )
    ) {
      insertStmt.setString(1, title);
      insertStmt.setString(2, content);
      insertStmt.setBoolean(3, isPublished);
      insertStmt.setTimestamp(4, Utils.dateToTimestamp(created));
      insertStmt.setString(5, author.getUserName());
      insertStmt.executeUpdate();

      return new BlogPosts(
        Utils.getAutoIncrementKey(insertStmt),
        title,
        content,
        isPublished,
        created,
        author
      );
    }
  }

  public static BlogPosts updateContent(
    Connection cxn,
    BlogPosts blogPost,
    String newContent
  ) throws SQLException {
    final String updateBlogPost =
      "UPDATE BlogPosts SET Content = ?, Created = ? WHERE PostId = ?;";

    try (
      PreparedStatement updateStmt = cxn.prepareStatement(updateBlogPost)
    ) {
      updateStmt.setString(1, newContent);
      // Sets the Created timestamp to the current time.
      Date newCreatedTimestamp = new Date();
      updateStmt.setTimestamp(2, new Timestamp(newCreatedTimestamp.getTime()));
      updateStmt.setInt(3, blogPost.getPostId());
      updateStmt.executeUpdate();

      // Update the blogPost param before returning to the caller.
      blogPost.setContent(newContent);
      blogPost.setCreated(newCreatedTimestamp);
      return blogPost;
    }
  }

  /**
   * Delete the BlogPosts instance.
   */
  public static void delete(Connection cxn, BlogPosts blogPost) throws SQLException {
    // Note: BlogComments has a fk constraint on BlogPosts with the reference option
    // ON DELETE CASCADE. So this delete operation will delete all the referencing
    // BlogComments.
    final String deleteBlogPost = "DELETE FROM BlogPosts WHERE PostId = ?;";

    try (
      PreparedStatement deleteStmt = cxn.prepareStatement(deleteBlogPost)
    ) {
      deleteStmt.setInt(1, blogPost.getPostId());
      deleteStmt.executeUpdate();
    }
  }

  /**
   * Get the BlogPosts record by fetching it from your MySQL instance.
   * This runs a SELECT statement and returns a single BlogPosts instance.
   * Note that we use BlogUsersDao to retrieve the referenced BlogUsers instance.
   * One alternative (possibly more efficient) is using a single SELECT statement
   * to join the BlogPosts, BlogUsers tables and then build each object.
   */
  public static BlogPosts getBlogPostById(Connection cxn, int postId) throws SQLException {
    final String selectBlogPost = """
      SELECT PostId, Title, Content, Published, Created, UserName
        FROM BlogPosts
        WHERE PostId = ?;""";
    try (
      PreparedStatement selectStmt = cxn.prepareStatement(selectBlogPost)
    ) {
      selectStmt.setInt(1, postId);

      try (ResultSet results = selectStmt.executeQuery()) {
        if (results.next()) {
          return new BlogPosts(
            results.getInt("PostId"),
            results.getString("Title"),
            results.getString("Content"),
            results.getBoolean("Published"),
            Utils.timestampToDate(results.getTimestamp("Created")),
            BlogUsersDao.getBlogUserFromUserName(cxn, results.getString("UserName"))
          );
        } else {
          return null;
        }
      }
    }
  }

  /**
   * Get the all the BlogPosts for a user.
   */
  public static List<BlogPosts> getBlogPostsForUser(
    Connection cxn,
    BlogUsers blogUser
  ) throws SQLException {
    List<BlogPosts> blogPosts = new ArrayList<>();
    String selectBlogPosts = """
      SELECT PostId, Title, Content, Published, Created, UserName
        FROM BlogPosts
        WHERE UserName = ?;""";
    try (PreparedStatement selectStmt = cxn.prepareStatement(selectBlogPosts)) {
      selectStmt.setString(1, blogUser.getUserName());
      try (ResultSet results = selectStmt.executeQuery()) {
        while (results.next()) {
          blogPosts.add(
            new BlogPosts(
              results.getInt("PostId"),
              results.getString("Title"),
              results.getString("Content"),
              results.getBoolean("Published"),
              Utils.timestampToDate(results.getTimestamp("Created")),
              blogUser
            )
          );
        }
        return blogPosts;
      }
    }
  }
}
