package other;

import java.io.Serializable;
import java.util.Objects;

public class Comment implements Serializable {
    private User owner;

    public Comment(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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
