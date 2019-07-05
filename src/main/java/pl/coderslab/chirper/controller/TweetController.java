package pl.coderslab.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.chirper.entity.Comment;
import pl.coderslab.chirper.entity.Tweet;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.payload.CommentRequest;
import pl.coderslab.chirper.payload.TweetRequest;
import pl.coderslab.chirper.repository.CommentRepository;
import pl.coderslab.chirper.repository.TweetRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.UserPrincipal;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    //TODO REFACTOR RESPONSE OBJECTS SO DON'T CONTAIN UNNECESSARY INFORMATION LIKE PASSWORD

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public TweetController(TweetRepository tweetRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    //test URI
    @RequestMapping("/test")
    public boolean test(@AuthenticationPrincipal UserPrincipal user){
        System.out.println(user.getFirstName() +  " " + user.getLastName() + " " + user.getId());
        return true;
    }

    @GetMapping()
    public List<Tweet> getTweets(){
        return tweetRepository.findAll();
    }

    @PostMapping()
    @Secured("ROLE_USER")
    public Tweet addTweet(@AuthenticationPrincipal UserPrincipal user, @RequestBody Tweet tweet){
        tweet.setUser(userRepository.findUserById(user.getId()).get());
        tweetRepository.save(tweet);
        return tweet;
    }

    @DeleteMapping("/{tweetId}")
    @Secured("ROLE_USER")
    public void delete(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long tweetId){
        User author = userRepository.findUserById(user.getId()).get();
        Tweet tweetToDelete = tweetRepository.findById(tweetId).get();
        if(tweetToDelete.getUser().getId().equals(tweetRepository.findById(tweetId).get().getUser().getId())) {
            tweetRepository.delete(tweetToDelete);
            System.out.println("Tweet deleted");
        } else {
            System.out.println("Cannot be deleted. You are not the owner.");
        }
    }

    @PutMapping()
    @Secured("ROLE_USER")
    public Tweet update(@AuthenticationPrincipal UserPrincipal user, @RequestBody TweetRequest tweetRequest, HttpServletResponse servletResponse) throws IOException {
        User author = userRepository.findUserById(user.getId()).get();
        Tweet tweetToUpdate = tweetRepository.findById(tweetRequest.getId()).get();
        if(tweetToUpdate.getUser().getId().equals(author.getId())) {
            tweetToUpdate.setText(tweetRequest.getText());
            tweetRepository.save(tweetToUpdate);
            System.out.println("Tweet updated");
            return tweetToUpdate;

        } else {
            System.out.println("Cannot be deleted. You are not the author.");
            servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not the author");
            return null;

        }
    }


    // comment controllers

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

    @PutMapping("/update-comment")
    @Secured("ROLE_USER")
    public void updateComment(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody CommentRequest commentRequest){

        Comment commentToUpdate = commentRepository.findById(commentRequest.getId()).get();
        commentToUpdate.setText(commentRequest.getText());
        commentRepository.save(commentToUpdate);

        System.out.println("Comment updated");
    }

    @DeleteMapping("/delete-comment")
    @Secured("ROLE_USER")
    public void deleteComment(@AuthenticationPrincipal UserPrincipal user, @RequestParam Long id){

        //Comment commentToDelete = commentRepository.findById(id).get();
        commentRepository.delete(commentRepository.findById(id).get());
        commentRepository.myDeleteById(id);

        System.out.println("Comment deleted");
    }

}
