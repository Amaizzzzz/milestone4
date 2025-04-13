package blog.model;

import java.util.Date;
import java.util.Objects;

public class Administrators extends Persons {
  private Date lastLogin;

  public Administrators(String userName, String firstName, String lastName, Date lastLogin) {
    super(userName, firstName, lastName);
    this.lastLogin = lastLogin;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
		return true;
	}
    if (o == null || getClass() != o.getClass() || !super.equals(o)) {
      return false;
    }
    Administrators that = (Administrators) o;
    return Objects.equals(lastLogin, that.lastLogin);
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException("not implemented yet");
  }

  @Override
  public String toString() {
    return String.format(
      "Administrators(%s, %s)",
      super.fieldsToString(),
      lastLogin
    );
  }
}
