package pl.coderslab.chirper.service;

import org.springframework.stereotype.Service;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.repository.UserRepository;

@Service
public class EmailService {

    private UserRepository userRepository;


    public EmailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }
}
