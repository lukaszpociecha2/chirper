package pl.coderslab.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.chirper.entity.Tweet;
import pl.coderslab.chirper.repository.TweetRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Autowired
    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
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

}
