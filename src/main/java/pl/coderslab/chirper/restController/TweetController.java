package pl.coderslab.chirper.restController;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coderslab.chirper.entity.Tweet;
import pl.coderslab.chirper.errors.TweetNotFoundException;
import pl.coderslab.chirper.errors.UserNotFoundException;
import pl.coderslab.chirper.payload.TweetRequest;
import pl.coderslab.chirper.repository.TweetRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.UserPrincipal;
import pl.coderslab.chirper.service.TweetService;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tweets")
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetRepository tweetRepository, UserRepository userRepository, TweetService tweetService) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.tweetService = tweetService;
    }

    @GetMapping()
    public List<Tweet> getTweets(){
        return tweetRepository.findAll();
    }

    @PostMapping()
    @Secured("ROLE_USER")
    public ResponseEntity<?> addTweet(@AuthenticationPrincipal UserPrincipal user, @RequestBody Tweet tweet){
        tweet.setUser(userRepository.findUserById(user.getId()).orElseThrow(()-> new UserNotFoundException(user.getId())));
        Tweet createdTweet = tweetRepository.save(tweet);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().path("/{id}").buildAndExpand(createdTweet.getId()).toUri();
        return ResponseEntity.created(location).body(tweet);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        Tweet tweet = tweetRepository.findById(id).orElseThrow(() -> new TweetNotFoundException(id));

        return ResponseEntity.ok(tweet);
    }

    @PutMapping("/{id}") // TODO remake to use id
    @Secured("ROLE_USER")
    public ResponseEntity<?> updateTweet(@AuthenticationPrincipal UserPrincipal user,
                                         @RequestBody TweetRequest tweetRequest,
                                         @PathVariable Long id,
                                         HttpServletResponse servletResponse) throws IOException {

        Tweet tweet = tweetService.updateTweet(user, tweetRequest);
        if (tweet!= null){
            return ResponseEntity.ok(tweet);
        }

        return ResponseEntity.badRequest().body("Cannot update. Only author can update");
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> deleteTweet(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id){

        Tweet tweet = tweetService.deleteTweet(user, id);
        return ResponseEntity.ok("Tweet deleted");
    }
}
