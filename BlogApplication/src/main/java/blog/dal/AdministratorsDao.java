package blog.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import blog.model.Administrators;

public class AdministratorsDao {
  private AdministratorsDao() { }

  public static Administrators create(
    Connection cxn,
    String userName,
    String firstName,
    String lastName,
    Date lastLogin
  ) throws SQLException {
    String insertAdministrator = "INSERT INTO Administrators (UserName, LastLogin) VALUES (?, ?);";

    try (PreparedStatement insertStmt = cxn.prepareStatement(insertAdministrator)) {
      PersonsDao.create(cxn, userName, firstName, lastName);
      insertStmt.setString(1, userName);
      insertStmt.setTimestamp(2, Utils.dateToTimestamp(lastLogin));
      insertStmt.executeUpdate();
      return new Administrators(userName, firstName, lastName, lastLogin);
    }
  }

  /**
   * Update the LastName of the Administrators instance.
   * This runs a UPDATE statement.
   */
  public static Administrators updateLastName(
    Connection cxn,
    Administrators administrator,
    String newLastName
  ) throws SQLException {
    // The field to update only exists in the superclass table, so we can
    // just call the superclass method.
    return PersonsDao.updateLastName(cxn, administrator, newLastName);
  }

  /**
   * Delete the Administrators instance.
   * This runs a DELETE statement.
   */
  public static void delete(
    Connection cxn,
    Administrators administrator
  ) throws SQLException {
    String deleteAdministrator = "DELETE FROM Administrators WHERE UserName = ?;";

    try (PreparedStatement deleteStmt = cxn.prepareStatement(deleteAdministrator)) {
      deleteStmt.setString(1, administrator.getUserName());
      deleteStmt.executeUpdate();

      // Then also delete from the superclass.
      // Note: due to the fk constraint (ON DELETE CASCADE), we could simply call
      // super.delete() without even needing to delete from Administrators first.
      PersonsDao.delete(cxn, administrator);
    }
  }

  public static Administrators getAdministratorFromUserName(
    Connection cxn,
    String userName
  ) throws SQLException {
    // To build an Administrator object, we need the Persons record, too.
    String selectAdministrator = """
      SELECT Administrators.UserName AS UserName, FirstName, LastName, LastLogin
        FROM Administrators INNER JOIN Persons
          ON Administrators.UserName = Persons.UserName
        WHERE Administrators.UserName = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectAdministrator)) {
      selectStmt.setString(1, userName);

      try (ResultSet results = selectStmt.executeQuery()) {
        if (results.next()) {
          return new Administrators(
            userName,
            results.getString("FirstName"),
            results.getString("LastName"),
            Utils.timestampToDate(results.getTimestamp("LastLogin"))
          );
        } else {
          return null;
        }
      }
    }
  }

  public static List<Administrators> getAdministratorsFromFirstName(
    Connection cxn,
    String firstName
  ) throws SQLException {
    List<Administrators> administrators = new ArrayList<>();
    String selectAdministrators = """
      SELECT Administrators.UserName AS UserName, FirstName, LastName, LastLogin
        FROM Administrators INNER JOIN Persons
          ON Administrators.UserName = Persons.UserName
        WHERE Persons.FirstName = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectAdministrators)) {
      selectStmt.setString(1, firstName);
      try (ResultSet results = selectStmt.executeQuery()) {
        while (results.next()) {
          administrators.add(
            new Administrators(
              results.getString("UserName"),
              firstName,
              results.getString("LastName"),
              Utils.timestampToDate(results.getTimestamp("LastLogin"))
            )
          );
        }
        return administrators;
      }
    }
  }
}
