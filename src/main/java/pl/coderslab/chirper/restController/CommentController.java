package pl.coderslab.chirper.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coderslab.chirper.entity.Comment;
import pl.coderslab.chirper.payload.CommentRequest;
import pl.coderslab.chirper.repository.CommentRepository;
import pl.coderslab.chirper.security.UserPrincipal;
import pl.coderslab.chirper.service.CommentService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/tweets")
public class CommentController {

    private CommentRepository commentRepository;
    private CommentService commentService;

    @Autowired
    public CommentController(CommentRepository commentRepository, CommentService commentService) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    @PostMapping("/{tweetId}/comment")
    @Secured("ROLE_USER")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserPrincipal user,
                                              @Valid @RequestBody CommentRequest commentRequest){
        Comment comment = commentService.createComment(user, commentRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id")
                .buildAndExpand(comment.getId()).toUri();
        return ResponseEntity.ok().location(location).body(comment);
    }

    @PutMapping("/{tweetId}/comment")
    @Secured("ROLE_USER")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody CommentRequest commentRequest){

        Comment commentToUpdate = commentRepository.findById(commentRequest.getId()).get();
        commentToUpdate.setText(commentRequest.getText());
        Comment result = commentRepository.save(commentToUpdate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).body(result);
    }

    @DeleteMapping("/{tweetId}/comment/{id}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id){

        //commentRepository.myDeleteById(id); // TODO for some reason the built-in delete in JPARepository is not working
        commentRepository.deleteById(id);
        return ResponseEntity.ok().body("Comment deleted");
    }

}
