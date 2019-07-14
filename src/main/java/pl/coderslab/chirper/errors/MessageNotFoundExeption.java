package pl.coderslab.chirper.errors;

public class MessageNotFoundExeption extends RuntimeException {
    public MessageNotFoundExeption(Long id) {
        super("Cannot find message with id " + id);
    }
}
