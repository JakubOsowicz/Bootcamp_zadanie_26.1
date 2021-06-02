package pl.osowicz.task_manager.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.osowicz.task_manager.user.User;
import pl.osowicz.task_manager.user.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findAllByEmail(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<SimpleGrantedAuthority> roles = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                    .collect(Collectors.toSet());

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
        }
        throw new UsernameNotFoundException("username" + username + " not found");
    }
}
