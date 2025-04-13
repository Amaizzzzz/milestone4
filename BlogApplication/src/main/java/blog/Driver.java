package blog;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import blog.dal.AdministratorsDao;
import blog.dal.BlogCommentsDao;
import blog.dal.BlogPostsDao;
import blog.dal.BlogUsersDao;
import blog.dal.ConnectionManager;
import blog.dal.PersonsDao;
import blog.dal.ResharesDao;
import blog.model.Administrators;
import blog.model.BlogComments;
import blog.model.BlogPosts;
import blog.model.BlogUsers;
import blog.model.Persons;
import blog.model.Reshares;

public class Driver {
  public static void main(String[] args) {
    try {
      resetSchema();
      insertRecords();
    } catch (SQLException e) {
      System.out.print("SQL Exception: ");
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public static void insertRecords() throws SQLException {
    try (
      Connection cxn = ConnectionManager.getConnection()
    ) {
      // INSERT objects from our model.
      Date date = new Date();
      AdministratorsDao.create(cxn, "a", "bruce", "chhay_a", date);
      AdministratorsDao.create(cxn, "a1", "bruce", "chhay_a1", date);
      AdministratorsDao.create(cxn, "a2", "bruce", "chhay_a2", date);
      BlogUsers blogUser = BlogUsersDao.create(cxn, "bu", "bruce", "chhay", date,
        BlogUsers.StatusLevel.novice);
      BlogUsers blogUser1 = BlogUsersDao.create(cxn, "bu1", "bruce", "chhay_bu1", date,
        BlogUsers.StatusLevel.intermediate);
      BlogUsers blogUser2 = BlogUsersDao.create(cxn, "bu2", "bruce", "chhay_bu2", date,
        BlogUsers.StatusLevel.advanced);
      BlogPosts blogPost = BlogPostsDao.create(cxn, "Laser Cats", "content", true, date, blogUser);
      BlogPosts blogPost1 = BlogPostsDao.create(cxn, "Dancing Cats", "content1", true, date, blogUser);
      BlogPosts blogPost2 = BlogPostsDao.create(cxn, "Sleeping Cats", "content2", true, date, blogUser);
      BlogCommentsDao.create(cxn, "Comment", date, blogPost, blogUser1);
      BlogCommentsDao.create(cxn, "Comment1", date, blogPost, blogUser1);
      BlogCommentsDao.create(cxn, "Comment2", date, blogPost, blogUser1);
      BlogCommentsDao.create(cxn, "Comment3", date, blogPost, blogUser);
      BlogCommentsDao.create(cxn, "Comment4", date, blogPost, blogUser);
      BlogCommentsDao.create(cxn, "Comment5", date, blogPost, blogUser);
      ResharesDao.create(cxn, blogUser2, blogPost);
      ResharesDao.create(cxn, blogUser2, blogPost1);
      ResharesDao.create(cxn, blogUser2, blogPost2);

      // READ.
      Persons p1 = PersonsDao.getPersonFromUserName(cxn, "bu");
      List<Persons> pList1 = PersonsDao.getPersonsFromFirstName(cxn, "bruce");
      System.out.format("Reading person: u:%s f:%s l:%s \n",
        p1.getUserName(), p1.getFirstName(), p1.getLastName());
      for (Persons p : pList1) {
        System.out.format("Looping persons: u:%s f:%s l:%s \n",
          p.getUserName(), p.getFirstName(), p.getLastName());
      }
      Administrators a1 = AdministratorsDao.getAdministratorFromUserName(cxn, "a");
      List<Administrators> aList1 = AdministratorsDao.getAdministratorsFromFirstName(cxn, "bruce");
      System.out.format("Reading administrator: u:%s f:%s l:%s d:%s \n",
        a1.getUserName(), a1.getFirstName(), a1.getLastName(), a1.getLastLogin());
      for (Administrators a : aList1) {
        System.out.format("Looping administrators: u:%s f:%s l:%s \n",
          a.getUserName(), a.getFirstName(), a.getLastName(), a.getLastLogin());
      }
      BlogUsers bu1 = BlogUsersDao.getBlogUserFromUserName(cxn, "bu");
      List<BlogUsers> buList1 = BlogUsersDao.getBlogUsersFromFirstName(cxn, "bruce");
      System.out.format("Reading blog user: u:%s f:%s l:%s d:%s s:%s \n",
        bu1.getUserName(), bu1.getFirstName(), bu1.getLastName(), bu1.getDob(),
        bu1.getStatusLevel().name());
      for (BlogUsers bu : buList1) {
        System.out.format("Looping blog users: u:%s f:%s l:%s d:%s s:%s \n",
          bu.getUserName(), bu.getFirstName(), bu.getLastName(), bu.getDob(),
          bu.getStatusLevel().name());
      }
      List<BlogPosts> bpList1 = BlogPostsDao.getBlogPostsForUser(cxn, bu1);
      for (BlogPosts bp : bpList1) {
        System.out.format("Looping blog posts: t:%s c:%s u:%s \n",
          bp.getTitle(), bp.getContent(), bu1.getUserName());
      }
      List<BlogComments> bcList1 = BlogCommentsDao.getBlogCommentsForUser(cxn, blogUser1);
      for (BlogComments bc : bcList1) {
        System.out.format("Looping blog comments: t:%s u:%s \n",
          bc.getContent(), blogUser1.getUserName());
      }
      bcList1 = BlogCommentsDao.getBlogCommentsForUser(cxn, blogUser);
      for (BlogComments bc : bcList1) {
        System.out.format("Looping blog comments: t:%s u:%s \n",
          bc.getContent(), blogUser.getUserName());
      }
      List<Reshares> rList1 = ResharesDao.getResharesForUser(cxn, blogUser2);
      for (Reshares r : rList1) {
        System.out.format("Looping reshare: i:%s u:%s t:%s \n",
          r.getReshareId(), r.getBlogUser().getUserName(),
          r.getBlogPost().getTitle());
      }
    }
  }

  private static void resetSchema() throws SQLException{
    try (
      Connection cxn = ConnectionManager.getSchemalessConnection()
    ) {
      cxn.createStatement().executeUpdate(
        "DROP SCHEMA IF EXISTS BlogApplication;"
      );
      cxn.createStatement().executeUpdate("CREATE SCHEMA BLogApplication;");
    }

    try (
      Connection cxn = ConnectionManager.getConnection()
    ) {
      cxn.createStatement().executeUpdate("""
        CREATE TABLE Persons (
          UserName VARCHAR(255),
          FirstName VARCHAR(255),
          LastName VARCHAR(255),
          CONSTRAINT pk_Persons_UserName PRIMARY KEY (UserName)
        );"""
      );

      cxn.createStatement().executeUpdate("""
        CREATE TABLE Administrators (
          UserName VARCHAR(255),
          LastLogin TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          CONSTRAINT pk_Administrators_UserName
            PRIMARY KEY (UserName),
          CONSTRAINT fk_Administrators_UserName
            FOREIGN KEY (UserName)
            REFERENCES Persons(UserName)
            ON UPDATE CASCADE ON DELETE CASCADE
        );"""
      );

      cxn.createStatement().executeUpdate("""
        CREATE TABLE BlogUsers (
          UserName VARCHAR(255),
          DoB TIMESTAMP NOT NULL,
          StatusLevel ENUM ('novice', 'intermediate', 'advanced'),
          CONSTRAINT pk_BlogUsers_UserName
            PRIMARY KEY (UserName),
          CONSTRAINT fk_BlogUsers_UserName
            FOREIGN KEY (UserName)
            REFERENCES Persons(UserName)
            ON UPDATE CASCADE ON DELETE CASCADE
        );"""
      );

      cxn.createStatement().executeUpdate("""
        CREATE TABLE BlogPosts (
          PostId INT AUTO_INCREMENT,
          Title VARCHAR(255) NOT NULL,
          Content LONGTEXT,
          Published BOOLEAN DEFAULT FALSE,
          Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          UserName VARCHAR(255),
          CONSTRAINT pk_BlogPosts_PostId PRIMARY KEY (PostID),
          CONSTRAINT fk_BlogPosts_UserName
            FOREIGN KEY (UserName)
            REFERENCES BlogUsers(UserName)
            ON UPDATE CASCADE ON DELETE SET NULL
        );"""
      );

      cxn.createStatement().executeUpdate("""
        CREATE TABLE BlogComments (
          CommentId INT AUTO_INCREMENT,
          Content VARCHAR(255) NOT NULL,
          Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          UserName VARCHAR(255),
          PostId INT,
          CONSTRAINT pk_BlogComments_CommentId PRIMARY KEY (CommentId),
          CONSTRAINT fk_BlogComments_UserName FOREIGN KEY (UserName)
            REFERENCES BlogUsers(UserName)
            ON UPDATE CASCADE ON DELETE SET NULL,
          CONSTRAINT fk_BlogComments_PostId FOREIGN KEY (PostId)
            REFERENCES BlogPosts(PostId)
            ON UPDATE CASCADE ON DELETE CASCADE
        );"""
      );

      cxn.createStatement().executeUpdate("""
        CREATE TABLE Reshares (
          ReshareId INT AUTO_INCREMENT,
          UserName VARCHAR(255),
          PostId INT,
          Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          CONSTRAINT pk_Reshares_ReshareId
            PRIMARY KEY (ReshareId),
          CONSTRAINT fk_Reshares_UserName
            FOREIGN KEY (UserName)
            REFERENCES BlogUsers(UserName)
            ON UPDATE CASCADE ON DELETE SET NULL,
          CONSTRAINT fk_Reshares_PostId FOREIGN KEY (PostId)
            REFERENCES BlogPosts(PostId)
            ON UPDATE CASCADE ON DELETE SET NULL
        );"""
      );
    }
  }
}
