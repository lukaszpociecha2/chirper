package pl.coderslab.chirper.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override

    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmail(userEmail).orElseThrow(
                ()-> new UsernameNotFoundException("User not found with email: " + userEmail));
        System.out.println(user.getFirstName());
        return UserPrincipal.create(user);
    }

    // This method is used by JWTAuthenticationFilter

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }


}
