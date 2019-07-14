package pl.coderslab.chirper.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.coderslab.chirper.errors.MessageNotFoundExeption;

@ControllerAdvice
public class MessageNotFoundAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MessageNotFoundExeption.class)
    public String messageNotFoundHandler(MessageNotFoundExeption e){
        return e.getMessage();
    }

}
