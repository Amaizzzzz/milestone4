package blog.model;

import java.util.Date;
import java.util.Objects;

public class BlogPosts {
  private int postId;
  private String title;
  private String content;
  private boolean published;
  private Date created;
  private BlogUsers blogUser;

  public BlogPosts(int postId, String title, String content,
      boolean published, Date created, BlogUsers blogUser) {
    this.postId = postId;
    this.title = title;
    this.content = content;
    this.published = published;
    this.created = created;
    this.blogUser = blogUser;
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public BlogUsers getBlogUser() {
    return blogUser;
  }

  public void setBlogUser(BlogUsers blogUser) {
    this.blogUser = blogUser;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
		return true;
	}
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlogPosts blogPosts = (BlogPosts) o;
    return
      postId == blogPosts.postId &&
      published == blogPosts.published &&
      Objects.equals(title, blogPosts.title) &&
      Objects.equals(content, blogPosts.content) &&
      Objects.equals(created, blogPosts.created) &&
      Objects.equals(blogUser, blogPosts.blogUser);
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String toString() {
    return String.format(
      "BlogPosts(%d, \"%s\", \"%s\", %b, %s, %s)",
      postId,
      title,
      content,
      published,
      created,
      blogUser.getUserName()
    );
  }
}