package pl.coderslab.chirper.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coderslab.chirper.entity.Message;
import pl.coderslab.chirper.errors.MessageNotFoundExeption;
import pl.coderslab.chirper.payload.MessageRequest;
import pl.coderslab.chirper.payload.MessageResponse;
import pl.coderslab.chirper.repository.MessageRepository;
import pl.coderslab.chirper.security.UserPrincipal;
import pl.coderslab.chirper.service.MessageService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {


    private MessageRepository messageRepository;
    private MessageService messageService;

    @Autowired
    public MessageController(MessageRepository messageRepository, MessageService messageService) {
        this.messageRepository = messageRepository;
        this.messageService = messageService;
    }

    @GetMapping
    @Secured("ROLE_USER")
    public List<MessageResponse> getAllMessagesToActiveUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return messageService.getAllReceivedMessages(userPrincipal);
    }

    @GetMapping("/{id}")
    @Secured(("ROLE_USER"))
    public ResponseEntity<?> getOne(@PathVariable Long id){

        Message message = messageRepository.findById(id).orElseThrow(()->new MessageNotFoundExeption(id));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(id).toUri();

        return ResponseEntity.ok().location(location).body(message);
    }

    @PostMapping
    @Secured("ROLE_USER")
    public ResponseEntity<?> sendMessageToUser(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody MessageRequest messageRequest){
        Message message = messageService.sendMessage(userPrincipal, messageRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(message.getId()).toUri();

        return ResponseEntity.created(location).body(message);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> deleteMessage(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable(name = "id") Long messageId){
        messageService.deleteMesage(userPrincipal, messageId);

        return ResponseEntity.ok("Message deleted");
    }
}
