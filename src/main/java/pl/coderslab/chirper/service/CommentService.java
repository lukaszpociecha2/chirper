package pl.coderslab.chirper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.chirper.entity.Comment;
import pl.coderslab.chirper.entity.Tweet;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.errors.TweetNotFoundException;
import pl.coderslab.chirper.errors.UserNotFoundException;
import pl.coderslab.chirper.payload.CommentRequest;
import pl.coderslab.chirper.repository.CommentRepository;
import pl.coderslab.chirper.repository.TweetRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.UserPrincipal;

@Service
public class CommentService {

    private TweetRepository tweetRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    @Autowired
    public CommentService(TweetRepository tweetRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public Comment createComment(UserPrincipal user, CommentRequest commentRequest){

        Comment comment = new Comment();
        Tweet tweet = tweetRepository.findById(commentRequest.getTweetId()).orElseThrow(()->new TweetNotFoundException(commentRequest.getId()));
        User userFromRepo = userRepository.findUserById(user.getId()).orElseThrow(()-> new UserNotFoundException(user.getId()));
        comment.setUser(userFromRepo);
        comment.setTweet(tweet);
        comment.setText(commentRequest.getText());

        tweet.getComments().add(comment);

        return commentRepository.save(comment);
    }

}
