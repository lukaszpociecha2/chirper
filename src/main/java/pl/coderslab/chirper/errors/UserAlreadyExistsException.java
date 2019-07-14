package pl.coderslab.chirper.errors;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email){
        super("User already exists with email " + email);
    }

}
