package pl.coderslab.chirper.entity;

import lombok.Data;
import pl.coderslab.chirper.validator.NoSwearing;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NoSwearing
    private String text;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "tweet", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    List<Comment> comments = new ArrayList<>(); //TODO shouldn't be ThreadSafe list?


    private LocalDateTime created;

    public Tweet() {
    }

    @PrePersist
    private void setTweetTime(){
        this.created = LocalDateTime.now();
    }


}
