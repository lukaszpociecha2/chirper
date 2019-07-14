package pl.coderslab.chirper.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.coderslab.chirper.errors.TweetNotFoundException;

@ControllerAdvice
public class TweetNotFoundHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TweetNotFoundException.class)
    public String tweetNotFoundHanlder(TweetNotFoundException e) { return e.getMessage(); }

}
