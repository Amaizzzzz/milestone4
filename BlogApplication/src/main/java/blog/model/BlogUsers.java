package blog.model;

import java.util.Date;
import java.util.Objects;

public class BlogUsers extends Persons {
  private Date dob;
  private StatusLevel statusLevel;

  public enum StatusLevel {
    novice, intermediate, advanced
  }

  public BlogUsers(
    String userName,
    String firstName,
    String lastName,
    Date dob,
    StatusLevel statusLevel
  ) {
    super(userName, firstName, lastName);
    this.dob = dob;
    this.statusLevel = statusLevel;
  }

  public Date getDob() {
    return dob;
  }

  public void setDob(Date dob) {
    this.dob = dob;
  }

  public StatusLevel getStatusLevel() {
    return statusLevel;
  }

  public void setStatusLevel(StatusLevel statusLevel) {
    this.statusLevel = statusLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
		return true;
	}
    if (o == null || getClass() != o.getClass() || !super.equals(o)) {
      return false;
    }
    BlogUsers blogUsers = (BlogUsers) o;
    return Objects.equals(dob, blogUsers.dob) &&
           statusLevel == blogUsers.statusLevel;
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException("hashing not supported");
  }

  @Override
  public String toString() {
    return String.format(
      "BlogUsers(%s, %s, %s)",
      super.fieldsToString(),
      dob,
      statusLevel
    );
  }
}
