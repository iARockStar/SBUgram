package other;

import java.io.Serializable;
import java.util.Objects;

/**
 * this class is for comments and saving the information of one comment at a time.
 */
public class Comment implements Serializable {
    private String owner;
    private byte[] proPic;
    private Post post;
    private String description;

    public Comment(String owner, Post post,String description,byte[] proPic) {
        this.owner = owner;
        this.post = post;
        this.description = description;
        this.proPic = proPic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
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

    public byte[] getProPic() {
        return proPic;
    }

    public void setProPic(byte[] proPic) {
        this.proPic = proPic;
    }
}
