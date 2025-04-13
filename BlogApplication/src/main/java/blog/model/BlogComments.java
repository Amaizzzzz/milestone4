package blog.model;

import java.util.Date;
import java.util.Objects;

public class BlogComments {
  private int commentId;
  private String content;
  private Date created;
  private BlogPosts blogPost;
  private BlogUsers blogUser;

  public BlogComments(int commentId, String content, Date created, BlogPosts blogPost,
      BlogUsers blogUser) {
    this.commentId = commentId;
    this.content = content;
    this.created = created;
    this.blogPost = blogPost;
    this.blogUser = blogUser;
  }

  public int getCommentId() {
    return commentId;
  }

  public void setCommentId(int commentId) {
    this.commentId = commentId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public BlogPosts getBlogPost() {
    return blogPost;
  }

  public void setBlogPost(BlogPosts blogPost) {
    this.blogPost = blogPost;
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
    BlogComments that = (BlogComments) o;
    return commentId == that.commentId && Objects.equals(content, that.content)
           && Objects.equals(created, that.created) && Objects.equals(blogPost,
      that.blogPost) && Objects.equals(blogUser, that.blogUser);
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String toString() {
    return String.format(
      "BlogComments(%d, \"%s\", %s, %d, %s)",
      commentId,
      content,
      created,
      blogPost.getPostId(),
      blogUser.getUserName()
    );
  }
}
