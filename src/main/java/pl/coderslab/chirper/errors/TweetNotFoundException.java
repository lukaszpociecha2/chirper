package pl.coderslab.chirper.errors;

public class TweetNotFoundException extends RuntimeException {

    public TweetNotFoundException(Long id){
        super("Tweet not found with id " + id);
    }
}
