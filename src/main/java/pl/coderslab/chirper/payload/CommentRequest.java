package pl.coderslab.chirper.payload;

import pl.coderslab.chirper.entity.Tweet;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.validator.NoSwearing;

public class CommentRequest {

    private Long id;
    @NoSwearing
    private String text;
    private User user;
    private Long tweetId;

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

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }
}
