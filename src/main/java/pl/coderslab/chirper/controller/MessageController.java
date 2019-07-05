package pl.coderslab.chirper.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.chirper.entity.Message;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.payload.MessageRequest;
import pl.coderslab.chirper.payload.MessageResponse;
import pl.coderslab.chirper.repository.MessageRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.UserPrincipal;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private UserRepository userRepository;
    private MessageRepository messageRepository;

    public MessageController(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }


    @PostMapping()
    @Secured("ROLE_USER")
    public void sendMessageToUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody MessageRequest messageRequest){
        User author = userRepository.findUserById(userPrincipal.getId()).get();
        Message message = new Message();
        message.setAuthor(author);
        message.setRecepient(userRepository.findUserById(messageRequest.getRecepientId()).get());
        message.setText(messageRequest.getText());
        messageRepository.save(message);
    }

    @GetMapping()
    @Secured("ROLE_USER")
    public List<MessageResponse> getAllMessagesToActiveUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
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

    @DeleteMapping()
    @Secured("ROLE_USER")
    public void deleteMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long messageId){

        Message message = messageRepository.findById(messageId).get();
        if(message.getRecepient().getId()==userRepository.findUserById(userPrincipal.getId()).get().getId()){
            messageRepository.delete(message);
        } else {
            System.out.println("You are not the recepient.");
        }
    }




}
