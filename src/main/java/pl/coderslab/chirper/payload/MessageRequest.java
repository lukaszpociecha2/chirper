package pl.coderslab.chirper.payload;

import pl.coderslab.chirper.entity.User;

public class MessageRequest {

    private Long id;

    private String text;

    private User author;

    private User recepient;

    private Long recepientId;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getRecepient() {
        return recepient;
    }

    public void setRecepient(User recepient) {
        this.recepient = recepient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecepientId() {
        return recepientId;
    }

    public void setRecepientId(Long recepientId) {
        this.recepientId = recepientId;
    }
}
