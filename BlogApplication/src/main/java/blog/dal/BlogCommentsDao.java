package blog.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import blog.model.BlogComments;
import blog.model.BlogPosts;
import blog.model.BlogUsers;

public class BlogCommentsDao {
  private BlogCommentsDao() {   }

  public static BlogComments create(
    Connection cxn,
    final String content,
    final Date created,
    final BlogPosts post,
    final BlogUsers author
  ) throws SQLException {
    String insertBlogComment = """
      INSERT INTO BlogComments (Content,Created,UserName,PostId)
        VALUES (?, ?, ?, ?);""";

    try (
      PreparedStatement insertStmt = cxn.prepareStatement(
        insertBlogComment,
        Statement.RETURN_GENERATED_KEYS
      )
    ) {
      insertStmt.setString(1, content);
      insertStmt.setTimestamp(2, Utils.dateToTimestamp(created));
      insertStmt.setString(3, author.getUserName());
      insertStmt.setInt(4, post.getPostId());
      insertStmt.executeUpdate();

      return new BlogComments(
        Utils.getAutoIncrementKey(insertStmt),
        content,
        created,
        post,
        author
      );
    }
  }

  /**
   * Update the content of the BlogComments instance.
   * This runs a UPDATE statement.
   */
  public static BlogComments updateContent(
    Connection cxn,
    BlogComments blogComment,
    String newContent
  ) throws SQLException {
    String updateBlogComment =
      "UPDATE BlogComments SET Content = ?,Created = ? WHERE CommentId = ?;";
    try (
      PreparedStatement updateStmt = cxn.prepareStatement(updateBlogComment)
    ) {
      Date newCreatedTimestamp = new Date();

      updateStmt.setString(1, newContent);
      updateStmt.setTimestamp(2, Utils.dateToTimestamp(newCreatedTimestamp));
      updateStmt.setInt(3, blogComment.getCommentId());
      updateStmt.executeUpdate();

      // Update the blogComment param before returning to the caller.
      blogComment.setContent(newContent);
      blogComment.setCreated(newCreatedTimestamp);
      return blogComment;
    }
  }

  /**
   * Delete the BlogComments instance.
   * This runs a DELETE statement.
   */
  public static void delete(
    Connection cxn,
    BlogComments blogComment
  ) throws SQLException {
    final String deleteBlogComment = "DELETE FROM BlogComments WHERE CommentId = ?;";

    try (
      PreparedStatement deleteStmt = cxn.prepareStatement(deleteBlogComment);
    ) {
      deleteStmt.setInt(1, blogComment.getCommentId());
      deleteStmt.executeUpdate();
    }
  }

  /**
   * Get the BlogComments record by fetching it from your MySQL instance.
   * This runs a SELECT statement and returns a single BlogComments instance.
   * Note that we use BlogPostsDao and BlogUsersDao to retrieve the referenced
   * BlogPosts and BlogUsers instances.
   * One alternative (possibly more efficient) is using a single SELECT statement
   * to join the BlogComments, BlogPosts, BlogUsers tables and then build each object.
   */
  public static BlogComments getBlogCommentById(
    Connection cxn,
    int commentId
  ) throws SQLException {
    String selectBlogComment = """
      SELECT CommentId, Content, Created, UserName, PostId
        FROM BlogComments
        WHERE CommentId = ?;""";

    try (
      PreparedStatement selectStmt = cxn.prepareStatement(selectBlogComment);
    ) {
      selectStmt.setInt(1, commentId);

      try (
        ResultSet results = selectStmt.executeQuery()
      ) {
        if (results.next()) {
          return new BlogComments(
            results.getInt("CommentId"),
            results.getString("Content"),
            Utils.timestampToDate(results.getTimestamp("Created")),
            BlogPostsDao.getBlogPostById(cxn, results.getInt("PostId")),
            BlogUsersDao.getBlogUserFromUserName(cxn, results.getString("UserName"))
          );
        } else {
          return null;
        }
      }
    }
  }

  /**
   * Get the all the BlogComments for a user.
   */
  public static List<BlogComments> getBlogCommentsForUser(
    Connection cxn,
    BlogUsers blogUser
  ) throws SQLException {
    String selectBlogComments = """
      SELECT CommentId, Content, Created, UserName, PostId
        FROM BlogComments
        WHERE UserName = ?;""";

    try (
      PreparedStatement selectStmt = cxn.prepareStatement(selectBlogComments);
    ) {
      selectStmt.setString(1, blogUser.getUserName());

      try (
        ResultSet results = selectStmt.executeQuery()
      ) {
        List<BlogComments> blogComments = new ArrayList<>();
        while (results.next()) {
          blogComments.add(
            new BlogComments(
              results.getInt("CommentId"),
              results.getString("Content"),
              Utils.timestampToDate(results.getTimestamp("Created")),
              BlogPostsDao.getBlogPostById(cxn, results.getInt("PostId")),
              blogUser
            )
          );
        }
        return blogComments;
      }
    }
  }

  /**
   * Get the all the BlogComments for a post.
   */
  public static List<BlogComments> getBlogCommentsForPost(
    Connection cxn,
    BlogPosts blogPost
  ) throws SQLException {
    String selectBlogPosts = """
      SELECT CommentId, Content, Created, UserName, PostId
        FROM BlogComments
        WHERE PostId = ?;""";

    try (
      PreparedStatement selectStmt = cxn.prepareStatement(selectBlogPosts);
    ) {
      selectStmt.setInt(1, blogPost.getPostId());

      try (
        ResultSet results = selectStmt.executeQuery();
      ) {
        List<BlogComments> blogComments = new ArrayList<>();
        while (results.next()) {
          blogComments.add(
            new BlogComments(
              results.getInt("CommentId"),
              results.getString("Content"),
              Utils.timestampToDate(results.getTimestamp("Created")),
              blogPost,
              BlogUsersDao.getBlogUserFromUserName(
                cxn,
                results.getString("UserName")
              )
            )
          );
        }
        return blogComments;
      }
    }
  }
}
