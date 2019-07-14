package pl.coderslab.chirper.restController;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.errors.UserNotFoundException;
import pl.coderslab.chirper.payload.CustomResponse;
import pl.coderslab.chirper.payload.SignUpRequest;
import pl.coderslab.chirper.payload.UpdateUserRequest;
import pl.coderslab.chirper.payload.UserResourceAssembler;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.UserPrincipal;
import pl.coderslab.chirper.service.EmailService;
import pl.coderslab.chirper.service.UserService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;
    private EmailService emailService;
    private UserRepository userRepository;
    private final UserResourceAssembler userResourceAssembler;

    public UserController(UserService userService, EmailService emailService, UserRepository userRepository, UserResourceAssembler userResourceAssembler) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userResourceAssembler = userResourceAssembler;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        User result = userService.createAccount(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/user/{id}")
                .buildAndExpand(result.getId()).toUri();

        Long id = result.getId();
        Resource<User> resource = new Resource<User>(result,
                ControllerLinkBuilder.linkTo(methodOn(UserController.class).getOne(id)).withSelfRel(),
                ControllerLinkBuilder.linkTo(methodOn(UserController.class).getAll()).withRel("users")
                );


        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/api/user/{id}").
                                                    buildAndExpand(user.getId()).toUri();
        Resource<User> userResource = userResourceAssembler.toResource(user);

        return ResponseEntity.ok(userResource);
    }

    @GetMapping
    @Secured("ROLE_USER")
    public ResponseEntity<?> getAll(){

        List<Resource<User>> userResources = userRepository.findAll().stream()
                .map(userResourceAssembler :: toResource
                ).collect(Collectors.toList());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/api/user").build().toUri();

        return ResponseEntity.status(200).location(location).body(userResources);
    }

    @PutMapping
    @Secured("ROLE_USER")
    public ResponseEntity<?> updateUserData(@AuthenticationPrincipal UserPrincipal user, @RequestBody UpdateUserRequest updateUser){

        User result = userService.updateUserData(updateUser, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location).body(new CustomResponse(true, "User data successfully updated"));
    }

    @DeleteMapping
    @Secured("ROLE_USER")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserPrincipal user){
        userService.deleteUser(user);
        return ResponseEntity.ok().body("User deleted");
    }

    @GetMapping("/validate-email")
    public boolean checkEmail(@RequestParam String email) {
        boolean emailExists = emailService.emailExists(email);
        System.out.println(emailExists);
        return emailExists;
    }
}
