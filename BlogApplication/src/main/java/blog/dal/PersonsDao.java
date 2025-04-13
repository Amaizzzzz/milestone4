package blog.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import blog.model.Persons;

public class PersonsDao {
  protected PersonsDao() {}

  /**
   * Save the Persons instance by storing it in your MySQL instance.
   * This runs a INSERT statement.
   */
  public static void create(
    Connection cxn,
    String userName,
    String firstName,
    String lastName
  ) throws SQLException {
    final String insertPerson =
      "INSERT INTO Persons (UserName, FirstName, LastName) VALUES (?, ?, ?);";

    try (PreparedStatement insertStmt = cxn.prepareStatement(insertPerson)) {
      insertStmt.setString(1, userName);
      insertStmt.setString(2, firstName);
      insertStmt.setString(3, lastName);
      insertStmt.executeUpdate();
    }
  }

  /**
   * Update the LastName of the Persons instance.
   * This runs a UPDATE statement.
   */
  public static <T extends Persons> T updateLastName(
    Connection cxn,
    T person,
    String newLastName
  ) throws SQLException {
    final String updatePerson =
      "UPDATE Persons SET LastName = ? WHERE UserName = ?;";

    try (PreparedStatement updateStmt = cxn.prepareStatement(updatePerson)) {
      updateStmt.setString(1, newLastName);
      updateStmt.setString(2, person.getUserName());
      updateStmt.executeUpdate();

      // Update the person param before returning to the caller.
      person.setLastName(newLastName);
      return person;
    }
  }

  /**
   * Delete the Persons instance.
   * This runs a DELETE statement.
   */
  public static void delete(
    Connection cxn,
    Persons person
  ) throws SQLException {
    deleteByUserName(cxn, person.getUserName());
  }

  public static void deleteByUserName(
    Connection cxn,
    String userName
  ) throws SQLException {
    String deletePerson = "DELETE FROM Persons WHERE UserName = ?;";

    try (PreparedStatement deleteStmt = cxn.prepareStatement(deletePerson)) {
      deleteStmt.setString(1, userName);
      deleteStmt.executeUpdate();
    }
  }

  /**
   * Get the Persons record by fetching it from your MySQL instance.
   * This runs a SELECT statement and returns a single Persons instance.
   */
  public static Persons getPersonFromUserName(
    Connection cxn,
    String userName
  ) throws SQLException {
    final String selectPerson =
      "SELECT UserName, FirstName, LastName FROM Persons WHERE UserName = ?;";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectPerson)) {
      selectStmt.setString(1, userName);

      try (ResultSet results = selectStmt.executeQuery()) {
        // You can iterate the result set (although the example below only retrieves
        // the first record). The cursor is initially positioned before the row.
        // Furthermore, you can retrieve fields by name and by type.
        if (results.next()) {
          return new Persons(
            results.getString("UserName"),
            results.getString("FirstName"),
            results.getString("LastName")
          );
        } else {
          return null;
        }
      }
    }
  }

  /**
   * Get the matching Persons records by fetching from your MySQL instance.
   * This runs a SELECT statement and returns a list of matching Persons.
   */
  public static List<Persons> getPersonsFromFirstName(
    Connection cxn,
    String firstName
  ) throws SQLException {
    final List<Persons> persons = new ArrayList<>();
    final String selectPersons =
      "SELECT UserName, FirstName, LastName FROM Persons WHERE FirstName = ?;";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectPersons)) {
      selectStmt.setString(1, firstName);

      try (ResultSet results = selectStmt.executeQuery()) {
        while (results.next()) {
          persons.add(
            new Persons(
              results.getString("UserName"),
              results.getString("FirstName"),
              results.getString("LastName")
            )
          );
        }
        return persons;
      }
    }
  }
}
