package company.trial.service;

import java.util.Collections;
import java.util.Random;

import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import company.trial.model.Role;
import company.trial.model.User;
import company.trial.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator userValidator;

    @Autowired
    private MailService mailService;

    @Override
    public void saveUser(User user) throws MessagingException {

        String code = generateCode();
        Errors errors = new BeanPropertyBindingResult(user, "user");
        ValidationUtils.invokeValidator(userValidator, user, errors);

        if (errors.hasErrors()) {

        }
        mailService.sendCode(user.getEmail(), user.getName(), code);

        user.setVerified(false);

        user.setVerificationCode(code);

        user.setRoles(Collections.singleton(Role.USER));

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
    public String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Override
    public boolean verify(User user) {

        String verificationCode = user.getVerificationCode();

        user = userRepository.findByVerificationCode(verificationCode);

        if (user != null) {
            user.setVerified(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean resetUserPassword(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.isVerified()) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }

        return false;
    }

   
}
