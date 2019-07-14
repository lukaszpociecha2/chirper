package pl.coderslab.chirper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.chirper.entity.Comment;
import pl.coderslab.chirper.entity.Tweet;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.entity.UserRole;
import pl.coderslab.chirper.errors.UserAlreadyExistsException;
import pl.coderslab.chirper.errors.UserNotFoundException;
import pl.coderslab.chirper.payload.SignUpRequest;
import pl.coderslab.chirper.payload.UpdateUserRequest;
import pl.coderslab.chirper.repository.CommentRepository;
import pl.coderslab.chirper.repository.TweetRepository;
import pl.coderslab.chirper.repository.UserRepository;
import pl.coderslab.chirper.repository.UserRoleRepository;
import pl.coderslab.chirper.security.UserPrincipal;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder;
    private UserRoleRepository roleRepository;
    private UserRepository userRepository;
    private TweetRepository tweetRepository;
    private CommentRepository commentRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRoleRepository roleRepository, UserRepository userRepository, TweetRepository tweetRepository, CommentRepository commentRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
        this.commentRepository = commentRepository;
    }

    public User createAccount(SignUpRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException(signUpRequest.getEmail());
        }

        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserRole userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("User Role not set.")); //TODO wlasny expception

        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }

    public User updateUserData(UpdateUserRequest updateUser, UserPrincipal user) {

        if (!userRepository.existsByEmail(updateUser.getEmail()) || updateUser.getEmail().equals(user.getEmail())) {
            User userToSave = userRepository.findUserById(user.getId()).get();
            userToSave.setFirstName(updateUser.getFirstName());
            userToSave.setLastName(updateUser.getLastName());
            userToSave.setEmail(updateUser.getEmail());
            userToSave.setPassword(updateUser.getPassword());
            userToSave.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            return userRepository.save(userToSave);
        } else {
            throw new UserAlreadyExistsException(updateUser.getEmail());
        }
    }

    public User deleteUser(UserPrincipal user){

        User userToDelete = userRepository.findUserById(user.getId()).orElseThrow(()-> new UserNotFoundException(user.getId()));
        userToDelete.getRoles().clear();

        Collection<Tweet> tweetCollection = tweetRepository.findAllByUserId(user.getId());
        tweetRepository.deleteAll(tweetCollection);

        Collection<Comment> commentCollection = commentRepository.findAllByUserId(user.getId());
        commentRepository.deleteAll(commentCollection);

        userRepository.delete(userToDelete);
        return userToDelete;

    }

}
