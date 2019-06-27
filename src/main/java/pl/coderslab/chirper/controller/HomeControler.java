package pl.coderslab.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.repository.UserRepository;

@RestController
public class HomeControler {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/add-user")
    @CrossOrigin
    public User addUser(){
        User user = new User();
        user.setFirstName("Lukasz");
        return user;
    }

    @PostMapping(value = "/add-user")
    @CrossOrigin(methods = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST}) //TODO configure CORS (default or open)
    public void home(@RequestBody String user){
        System.out.println(user);
        System.out.println("In HomeControl");
        
    }
}
