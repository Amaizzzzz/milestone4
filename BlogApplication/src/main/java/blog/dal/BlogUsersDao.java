package blog.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import blog.model.BlogUsers;

public class BlogUsersDao {
  private BlogUsersDao() { }

  public static BlogUsers create(
    Connection cxn,
    String userName,
    String firstName,
    String lastName,
    Date dateOfBirth,
    BlogUsers.StatusLevel statusLevel
  ) throws SQLException {
    final String insertBlogUser =
      "INSERT INTO BlogUsers (UserName, DoB, StatusLevel) VALUES (?, ?, ?);";

    try (PreparedStatement insertStmt = cxn.prepareStatement(insertBlogUser)) {
      PersonsDao.create(cxn, userName, firstName, lastName);
      insertStmt.setString(1, userName);
      insertStmt.setTimestamp(2, Utils.dateToTimestamp(dateOfBirth));
      insertStmt.setString(3, statusLevel.name());
      insertStmt.executeUpdate();
      return new BlogUsers(
        userName,
        firstName,
        lastName,
        dateOfBirth,
        statusLevel
      );
    }
  }

  public static BlogUsers updateLastName(
    Connection cxn,
    BlogUsers blogUser,
    String newLastName
  ) throws SQLException {
    // The field to update only exists in the superclass table, so we can
    // just call the superclass method.
    return PersonsDao.updateLastName(cxn, blogUser, newLastName);
  }

  /**
   * Delete the BlogUsers instance.  Returns the number of affected rows.
   */
  public static int delete(
    Connection cxn,
    BlogUsers blogUser
  ) throws SQLException {
    return deleteByUserName(cxn, blogUser.getUserName());
  }

  /**
   * Deletes the BlogUsers record with the specified userName.  Returns
   * the number of affected rows.
   */
  public static int deleteByUserName(
    Connection cxn,
    String userName
  ) throws SQLException {
    final String deleteBlogUser = "DELETE FROM BlogUsers WHERE UserName = ?;";

    try (PreparedStatement deleteStmt = cxn.prepareStatement(deleteBlogUser)) {
      deleteStmt.setString(1, userName);
      int numAffectedRows = deleteStmt.executeUpdate();

      if (numAffectedRows > 0) {
        // Then also delete from the superclass.  Note: due to the fk
        // constraint (ON DELETE CASCADE), we could simply call
        // PersonsDao.delete() without even needing to delete from
        // BlogUsers first.
        PersonsDao.deleteByUserName(cxn, userName);
      }

      return numAffectedRows;
    }
  }

  public static BlogUsers getBlogUserFromUserName(
    Connection cxn,
    String userName
  ) throws SQLException {
    // To build an BlogUser object, we need the Persons record, too.
    String selectBlogUser = """
      SELECT BlogUsers.UserName AS UserName,
             FirstName,
             LastName,
             DoB,
             StatusLevel
      FROM BlogUsers INNER JOIN Persons
        ON BlogUsers.UserName = Persons.UserName
      WHERE BlogUsers.UserName = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectBlogUser)) {
      selectStmt.setString(1, userName);

      try (ResultSet results = selectStmt.executeQuery()) {
        if (results.next()) {
          return new BlogUsers(
            userName,
            results.getString("firstName"),
            results.getString("lastName"),
            Utils.timestampToDate(results.getTimestamp("DoB")),
            BlogUsers.StatusLevel.valueOf(results.getString("StatusLevel"))
          );
        } else {
          return null;
        }
      }
    }
  }

  public static List<BlogUsers> getBlogUsersFromFirstName(
    Connection cxn,
    String firstName
  ) throws SQLException {
    List<BlogUsers> blogUsers = new ArrayList<>();
    String selectBlogUsers = """
      SELECT BlogUsers.UserName AS UserName,
             FirstName,
             LastName,
             DoB,
             StatusLevel
      FROM BlogUsers INNER JOIN Persons
        ON BlogUsers.UserName = Persons.UserName
      WHERE Persons.FirstName = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectBlogUsers)) {
      selectStmt.setString(1, firstName);
      try (ResultSet results = selectStmt.executeQuery()) {
        while (results.next()) {
          blogUsers.add(
            new BlogUsers(
              results.getString("userName"),
              firstName,
              results.getString("lastName"),
              Utils.timestampToDate(results.getTimestamp("DoB")),
              BlogUsers.StatusLevel.valueOf(results.getString("StatusLevel"))
            )
          );
        }
        return blogUsers;
      }
    }
  }
}
