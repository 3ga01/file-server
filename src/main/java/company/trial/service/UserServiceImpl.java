package company.trial.service;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import company.trial.model.Files;
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
    private JavaMailSender mailSender;

    @Override
    public void saveUser(User user) throws MessagingException {
        
        String code = generateCode();
        Errors errors = new BeanPropertyBindingResult(user, "user");
        ValidationUtils.invokeValidator(userValidator, user, errors);

        if (errors.hasErrors()) {

        }
        sendCode(user.getEmail(), user.getName(), code);

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
    public boolean isUserValidated(String email) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Override
    public void sendCode(String email, String userName, String code) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify your file sever account");
        message.setText("Dear " + userName + "\n" + "\n Welcome to file server! We're excited to have you join us.\n"
                + "\nTo complete your account setup, please use the verification code below: \n"
                + "\nVerification Code: "
                + code + "\n"
                + "\nPlease enter this code on the verification page to confirm your account and start using our service.\n"
                + "\nIf you didn't sign up for an account with us, please ignore this message. Someone may have used your email address by mistake, and no further action is required. \n"
                + "\nIf you have any questions or need assistance with your account, please contact our support team at emmanuel.omari@amalitech.org/+233 591 961 186.\n"
                + "\nThank you for choosing file Server. We look forward to serving you!\n" + "\nBest regards,\n"
                + "File Server team");
        mailSender.send(message);

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

    
}
