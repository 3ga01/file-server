package company.trial.service;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.User;

public interface UserService {
    void saveUser(User user) throws MessagingException;

    boolean userExist(String email);

    boolean isUserValidated(String email);

    String generateCode();

    boolean verify(User user);

    void findAllFiles();

    void findFileByEmail();

}
