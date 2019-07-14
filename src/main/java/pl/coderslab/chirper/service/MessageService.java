package pl.coderslab.chirper.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import pl.coderslab.chirper.entity.Message;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.errors.MessageNotFoundExeption;
import pl.coderslab.chirper.errors.UnauthorizedException;
import pl.coderslab.chirper.payload.MessageRequest;
import pl.coderslab.chirper.payload.MessageResponse;
import pl.coderslab.chirper.repository.MessageRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.UserPrincipal;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {


    private UserRepository userRepository;
    private MessageRepository messageRepository;

    public MessageService(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }


    public Message sendMessage(UserPrincipal userPrincipal, MessageRequest messageRequest){

        User author = userRepository.findUserById(userPrincipal.getId()).get();

        Message message = new Message();
            message.setAuthor(author);
            message.setRecepient(userRepository.findUserById(messageRequest.getRecepientId()).get());
            message.setText(messageRequest.getText());
        return messageRepository.save(message);

    }

    public List<MessageResponse> getAllReceivedMessages(UserPrincipal userPrincipal){
        List<Message> messages = messageRepository.findAllByRecepientId(userPrincipal.getId());
        List<MessageResponse> messageResponses = new ArrayList<>();
        for (Message message : messages) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setAuthorId(message.getAuthor().getId());
            messageResponse.setText(message.getText());
            messageResponse.setId(message.getId());
            messageResponses.add(messageResponse);
        }

        return messageResponses;
    }

    public Message deleteMesage(UserPrincipal userPrincipal, Long messageId){
        Message message = messageRepository.findById(messageId).orElseThrow(()-> new MessageNotFoundExeption(messageId));
        if(message.getRecepient().getId()==userRepository.findUserById(userPrincipal.getId()).get().getId()){
            messageRepository.delete(message);
            return message;
        } else {
            throw new UnauthorizedException("You are not the recepient of this message with id" + messageId);
        }
    }
}
