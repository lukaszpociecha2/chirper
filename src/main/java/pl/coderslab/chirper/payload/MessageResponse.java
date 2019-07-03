package pl.coderslab.chirper.payload;

import pl.coderslab.chirper.entity.Message;
import pl.coderslab.chirper.entity.User;

import java.util.List;

public class MessageResponse {


    private Long id;
    private String text;
    private Long authorId;
    private User recepient;

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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public User getRecepient() {
        return recepient;
    }

    public void setRecepient(User recepient) {
        this.recepient = recepient;
    }
}
