package pl.coderslab.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.repository.UserRepository;

@Controller
public class HomeControler {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    @ResponseBody
    @CrossOrigin //TODO configure CORS (default or open)
    public String home(){
        User user = new User();
        user.setFirstName("Lukasz");
        user.setLastName("Pociecha");
        user.setEmail("lukasz@pociecha.com");
        user.setPassword("password");

        userRepository.save(user);

        return "Welcome to chirper";

    }
}
