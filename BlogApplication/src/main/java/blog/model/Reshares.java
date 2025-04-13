package blog.model;

import java.util.Objects;

public class Reshares {
  private int reshareId;
  private BlogUsers blogUser;
  private BlogPosts blogPost;

  public Reshares(int reshareId, BlogUsers blogUser, BlogPosts blogPost) {
    this.reshareId = reshareId;
    this.blogUser = blogUser;
    this.blogPost = blogPost;
  }

  public int getReshareId() {
    return reshareId;
  }

  public void setReshareId(int reshareId) {
    this.reshareId = reshareId;
  }

  public BlogUsers getBlogUser() {
    return blogUser;
  }

  public void setBlogUser(BlogUsers blogUser) {
    this.blogUser = blogUser;
  }

  public BlogPosts getBlogPost() {
    return blogPost;
  }

  public void setBlogPost(BlogPosts blogPost) {
    this.blogPost = blogPost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
		return true;
	}
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Reshares reshares = (Reshares) o;
    return
      reshareId == reshares.reshareId &&
      Objects.equals(blogUser, reshares.blogUser) &&
      Objects.equals(blogPost, reshares.blogPost);
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException("hashing not supported");
  }

  @Override
  public String toString() {
    return String.format(
      "Reshares(%d, \"%s\", %d)",
      reshareId,
      blogUser.getUserName(),
      blogPost.getPostId()
    );
  }
}
