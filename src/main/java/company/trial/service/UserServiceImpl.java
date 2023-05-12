// package company.trial.service;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;

// import company.trial.model.User;
// import company.trial.repositories.UserRepository;
// @Service
// public class UserServiceImpl implements UserService {

//     @Autowired
//     private UserRepository userRepository;

//     @Override
//     public void saveUser(User user) {
//         user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//         userRepository.save(user);
//     }

//     // @Override
//     // public boolean isUserAlreadyPresent(String email) {
//     //     User user = userRepository.findByEmail(email);
//     //     return user.isPresent();
//     // }

//     // @Override
//     // public User getUserByEmail(String email) {
//     //     User user = userRepository.findByEmail(email);
//     //     return user.orElse(null);
//     // }
    
// }
