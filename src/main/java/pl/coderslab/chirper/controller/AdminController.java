package pl.coderslab.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@Secured("ROLE_ADMIN")
public class AdminController {

    private UserRepository userRepository;

    @Autowired
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/deleteUser")
    public void delete(@RequestParam Long id){
        User userToDelete = userRepository.findUserById(id).get();
        userToDelete.getRoles().clear();
        userRepository.delete(userToDelete);
        System.out.println("User deleted");

        //TODO skasowanie użytkownika powoduje, że próba wykorzystania tokena, kończy się:
        //Principal anonymousUser - AUTHORIZATION_FAILURE

        //SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }


}
