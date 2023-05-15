package company.trial.service;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;

import company.trial.model.User;

public interface UserService {
    void saveUser(User user) throws MessagingException;

    boolean userExist(String email);

    boolean isUserValidated(String email);

    String generateCode();

    void sendCode(String email, String userName,String code) throws MessagingException;

    boolean verify(User user);

    void findAllFiles();

    void findFileByEmail();


}
