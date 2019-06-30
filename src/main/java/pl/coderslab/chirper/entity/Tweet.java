package pl.coderslab.chirper.entity;

import pl.coderslab.chirper.validator.NoSwearing;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @NoSwearing
    private String text;

    @ManyToOne
    User user;

    @OneToMany(fetch = FetchType.EAGER)
    List<Comment> comments = new ArrayList<>();


    private LocalDateTime created;

    public Tweet() {
    }

    @PrePersist
    private void setTweetTime(){
        this.created = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
