package other;

import java.io.Serializable;
import java.util.Objects;

public class Comment implements Serializable {
    private User owner;
    private Post post;
    private String description;

    public Comment(User owner, Post post,String description) {
        this.owner = owner;
        this.post = post;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(owner, comment.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner);
    }
}
