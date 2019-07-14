package pl.coderslab.chirper.service;

import org.springframework.stereotype.Service;
import pl.coderslab.chirper.entity.Tweet;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.errors.TweetNotFoundException;
import pl.coderslab.chirper.errors.UserNotFoundException;
import pl.coderslab.chirper.payload.TweetRequest;
import pl.coderslab.chirper.repository.TweetRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.UserPrincipal;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@Service
public class TweetService {

    private UserRepository userRepository;
    private TweetRepository tweetRepository;

    public TweetService(UserRepository userRepository, TweetRepository tweetRepository) {
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }


    public Tweet deleteTweet(UserPrincipal user, Long tweetId) {
        User author = userRepository.findUserById(user.getId()).get();
        Tweet tweetToDelete = tweetRepository.findById(tweetId).get();
        if (tweetToDelete.getUser().getId().equals(tweetRepository.findById(tweetId).get().getUser().getId())) {
            tweetRepository.delete(tweetToDelete);
        } else {
            throw new NoSuchElementException();
        }

        return tweetToDelete;
    }

    public Tweet updateTweet(UserPrincipal user, TweetRequest tweetRequest){

        User author = userRepository.findUserById(user.getId()).orElseThrow(()-> new UserNotFoundException(user.getId()));

        Tweet tweetToUpdate = tweetRepository.findById(tweetRequest.getId()).orElseThrow(() -> new TweetNotFoundException(tweetRequest.getId()));

        if(tweetToUpdate.getUser().getId().equals(author.getId())) {

            tweetToUpdate.setText(tweetRequest.getText());
            tweetRepository.save(tweetToUpdate);
            return tweetToUpdate;
        } else {
            return null;
        }
    }
}
