package pl.coderslab.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.chirper.entity.Tweet;
import pl.coderslab.chirper.repository.TweetRepository;

import java.util.List;

@RestController
public class TweetController {

    private final TweetRepository tweetRepository;

    @Autowired
    public TweetController(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }


    @RequestMapping("/test")
    public boolean test(){
        return true;
    }

    @GetMapping("/tweets")
    @CrossOrigin
    public List<Tweet> getTweets(){
        return tweetRepository.findAll();
    }

    @PostMapping("/tweets/add")
    @CrossOrigin
    public void addTweet(){

    }
}
