package blog.model;

import java.util.Objects;

public class Persons {
  private String userName;
  private String firstName;
  private String lastName;

  public Persons(String userName, String firstName, String lastName) {
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Persons(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
		return true;
	}
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Persons persons = (Persons) o;
    return
      Objects.equals(userName, persons.userName) &&
      Objects.equals(firstName, persons.firstName) &&
      Objects.equals(lastName, persons.lastName);
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException("hashing not supported");
  }

  @Override
  public String toString() {
    return String.format("Persons(%s)", fieldsToString());
  }

  protected String fieldsToString() {
    return String.format(
      "\"%s\", \"%s\", \"%s\"",
      userName,
      firstName,
      lastName
    );
  }
}
