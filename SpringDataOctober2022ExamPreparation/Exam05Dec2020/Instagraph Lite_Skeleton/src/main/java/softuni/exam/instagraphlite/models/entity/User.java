package softuni.exam.instagraphlite.models.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String username;
    private String password;
    private Picture profilePicture;
    private Set<Post> posts;

    public User() {

    }

    @Column(length = 18, nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="profile_picture_id", referencedColumnName = "id")
    public Picture getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
    }

//    @OneToMany(mappedBy = "user")
    @OneToMany(mappedBy = "user", targetEntity = Post.class, fetch = FetchType.EAGER)
    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
