package blog.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import blog.model.BlogPosts;
import blog.model.BlogUsers;
import blog.model.Reshares;


public class ResharesDao {
  private ResharesDao() {}

  public static Reshares create(Connection cxn, BlogUsers user, BlogPosts post) throws SQLException {
    final String insertReshare = """
      INSERT INTO Reshares (UserName, PostID) VALUES (?, ?);
    """;
    try (
      PreparedStatement insertStmt =
        cxn.prepareStatement(insertReshare, Statement.RETURN_GENERATED_KEYS)
      )
    {
      insertStmt.setString(1, user.getUserName());
      insertStmt.setInt(2, post.getPostId());
      insertStmt.executeUpdate();

      return new Reshares(Utils.getAutoIncrementKey(insertStmt), user, post);
    }
  }

  /**
   * Delete the Reshares instance.
   * This runs a DELETE statement.
   */
  public static void delete(Connection cxn, Reshares reshare) throws SQLException {
    final String deleteReshare = "DELETE FROM Reshares WHERE ReshareId = ?;";
    try (
      PreparedStatement deleteStmt = cxn.prepareStatement(deleteReshare);
    ) {
      deleteStmt.setInt(1, reshare.getReshareId());
      deleteStmt.executeUpdate();
    }
  }

  /**
   * Get the Reshares record by fetching it from your MySQL instance.
   * This runs a SELECT statement and returns a single Reshares instance.
   * Note that we use BlogPostsDao and BlogUsersDao to retrieve the referenced
   * BlogPosts and BlogUsers instances.
   * One alternative (possibly more efficient) is using a single SELECT statement
   * to join the Reshares, BlogPosts, BlogUsers tables and then build each object.
   */
  public static Reshares getReshareById(
    Connection cxn,
    int reshareId
  ) throws SQLException {
    final String selectReshare = """
      SELECT ReshareId,UserName,PostId
        FROM Reshares
        WHERE ReshareId = ?;""";
    try (
      PreparedStatement selectStmt = cxn.prepareStatement(selectReshare)
      ) {
      selectStmt.setInt(1, reshareId);

      try (
        ResultSet results = selectStmt.executeQuery();
      ) {
        if (results.next()) {
          int resultReshareId = results.getInt("ReshareId");
          String userName = results.getString("UserName");
          int postId = results.getInt("PostId");

          BlogUsers blogUser = BlogUsersDao.getBlogUserFromUserName(cxn, userName);
          BlogPosts blogPost = BlogPostsDao.getBlogPostById(cxn, postId);
          return new Reshares(resultReshareId, blogUser, blogPost);
        } else {
          return null;
        }
      }
    }
  }

  /**
   * Get the all the Reshares for a user.
   */
  public static List<Reshares> getResharesForUser(
    Connection cxn,
    BlogUsers blogUser
  ) throws SQLException {
    List<Reshares> reshares = new ArrayList<>();
    String selectReshares = """
      SELECT ReshareId,UserName,PostId
        FROM Reshares
        WHERE UserName = ?;""";
    try (
      PreparedStatement selectStmt = cxn.prepareStatement(selectReshares)
    ) {
      selectStmt.setString(1, blogUser.getUserName());
      try (
        ResultSet results = selectStmt.executeQuery()
      ) {
        while (results.next()) {
          reshares.add(
            new Reshares(
              results.getInt("ReshareId"),
              blogUser,
              BlogPostsDao.getBlogPostById(cxn, results.getInt("PostId"))
            )
          );
        }
        return reshares;
      }
    }
  }
}
