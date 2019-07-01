package pl.coderslab.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.chirper.entity.Comment;
import pl.coderslab.chirper.entity.Tweet;
import pl.coderslab.chirper.payload.CommentRequest;
import pl.coderslab.chirper.repository.CommentRepository;
import pl.coderslab.chirper.repository.TweetRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.UserPrincipal;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public TweetController(TweetRepository tweetRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }


    @RequestMapping("/test")
    public boolean test(@AuthenticationPrincipal UserPrincipal user){
        System.out.println(user.getFirstName() +  " " + user.getLastName() + " " + user.getId());
        return true;
    }

    @GetMapping("/get-all")
    public List<Tweet> getTweets(){
        return tweetRepository.findAll();
    }

    @PostMapping("/add")
    @Secured("ROLE_USER")
    public void addTweet(@AuthenticationPrincipal UserPrincipal user, @RequestBody Tweet tweet){
        tweet.setUser(userRepository.findUserById(user.getId()).get());
        tweetRepository.save(tweet);
    }

    @Transactional
    @PostMapping("/add-comment")
    @Secured("ROLE_USER")
    public void addComment(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody CommentRequest commentRequest){
        Comment comment = new Comment();
        Tweet tweet = tweetRepository.findById(commentRequest.getTweetId()).get();
        comment.setUser(userRepository.findUserById(user.getId()).get());
        comment.setTweet(tweet);
        comment.setText(commentRequest.getText());

        tweet.getComments().add(comment);

        commentRepository.save(comment);

        System.out.println("Comment added");
    }

}
