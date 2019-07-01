package pl.coderslab.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.entity.UserRole;
import pl.coderslab.chirper.payload.*;
import pl.coderslab.chirper.repository.UserRoleRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.security.JwtTokenProvider;
import pl.coderslab.chirper.security.UserPrincipal;
import pl.coderslab.chirper.service.EmailService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth/**")
public class AuthController {

    //TODO pozamieniac wszystkie voidy na ResponseEntity<?>

    private UserRepository userRepository;
    private EmailService emailService;
    private UserRoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(UserRepository userRepository, EmailService emailService, UserRoleRepository roleRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new CustomResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserRole userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("User Role not set.")); //TODO wlasny expception

        user.setRoles(Collections.singleton(userRole));
        System.out.println(user.getRoles().size());

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location).body(new CustomResponse(true, "User registered successfully"));
    }

    @PutMapping("/update")
    @PatchMapping("/update") //TODO PATCH is not allowed (wlaczyć @RequestMapping(PUT,PATCH)
    @Secured("ROLE_USER")
    public void update(@AuthenticationPrincipal UserPrincipal user, @RequestBody UpdateUserRequest updateUser){

        System.out.println("updateUser firstName null:" + updateUser.getFirstName().isEmpty());

        //TODO do metody PATCH przychodzi tylko jeden wypelniony field (lub wiecej) wiec pola z UpdateUserRequest maja wartośc null lub moga przyjsc isEmpty
        //todo osbluzyc if'ami nullpointery
        // nie robić validatorem na poziomie parametrow metody, bo obiekt musialby miec wszystkie pola - tylko przy tworzeniu

        if(!userRepository.existsByEmail(updateUser.getEmail()) || updateUser.getEmail().equals(user.getEmail())){
            User userToSave = userRepository.findUserById(user.getId()).get();
            userToSave.setFirstName(updateUser.getFirstName());
            userToSave.setLastName(updateUser.getLastName());
            userToSave.setEmail(updateUser.getEmail());
            userToSave.setPassword(updateUser.getPassword());
            userToSave.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            userRepository.save(userToSave);
        }
        else {
            System.out.println("User email already exists"); //TODO throw new Error
        }
    }


    @DeleteMapping("/delete")
    @Transactional
    public void delete(@AuthenticationPrincipal UserPrincipal user){
        User userToDelete = userRepository.findUserById(user.getId()).get();
        userToDelete.getRoles().clear();
        userRepository.delete(userToDelete);
        System.out.println("User deleted");

        //TODO skasowanie użytkownika powoduje, że próba wykorzystania tokena, kończy się:
        //Principal anonymousUser - AUTHORIZATION_FAILURE

        //SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }

    /*@GetMapping("/api/foos")
    public Boolean foo(){
        return true;
    }

    @GetMapping("/api/admin/**")
    public Boolean admin(){
        return true;
    }


    @GetMapping("/add-user")
    @CrossOrigin
    public User addUser(){
        User user = new User();
        user.setFirstName("Lukasz");
        return user;
    }

    @PostMapping(value = "/add-user", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @CrossOrigin(methods = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST}) //TODO configure CORS (default or open)
    public void home(@RequestBody User user){
        System.out.println("In HomeControl");
        System.out.println(user.getFirstName());
        userRepository.save(user);

    } */

    @GetMapping("validate-email")
    //@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST}) //TODO configure CORS (default or open)
    public boolean checkEmail(@RequestParam String email){
        boolean emailExists = emailService.emailExists(email);
        System.out.println(emailExists);
        return emailExists;
    }



}
