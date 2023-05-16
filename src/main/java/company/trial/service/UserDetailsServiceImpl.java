package company.trial.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import company.trial.model.Role;
import company.trial.model.User;
import company.trial.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<String> roles = user.getRoles();
        Set<GrantedAuthority> authorities = new HashSet<>();
        
            boolean isAdmin = roles.contains("ROLE_ADMIN");
                        boolean isUser = roles.contains("ROLE_USER");


            if (isAdmin) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            }

            else if(isUser){ 
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            }

            else 
            throw new AccessDeniedException("You are not authorized");
        

        

        
        

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
    // Set<GrantedAuthority> authorities = new HashSet<>();
    // for (String role : user.getRoles()) {
    // authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
    // }
    // Set<GrantedAuthority> authorities = new HashSet<>();
    // for (String role : roles) {
    // authorities.add(new SimpleGrantedAuthority(role));
    // }

}
