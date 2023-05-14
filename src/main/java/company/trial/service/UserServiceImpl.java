package company.trial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import company.trial.model.User;
import company.trial.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator userValidator;

    @Override
    public void saveUser(User user) {
        Errors errors = new BeanPropertyBindingResult(user, "user");
        ValidationUtils.invokeValidator(userValidator, user, errors);

        if (errors.hasErrors()) {

        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean userExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserValidated(String email) {
        // TODO Auto-generated method stub
        return false;
    }

}
