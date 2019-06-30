package pl.coderslab.chirper.entity;

import pl.coderslab.chirper.validator.NoSwearing;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NoSwearing
    private String text;

    @ManyToOne
    private User user;

    @ManyToOne
    Tweet tweet;

    private LocalDateTime created;
    private LocalDateTime updated;

    @PrePersist
    private void setCommentCreated(){
        this.created = LocalDateTime.now();
    }

    @PreUpdate
    private void setCommentUpdated(){
        this.updated = LocalDateTime.now();
    }

    public Comment() {
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

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
