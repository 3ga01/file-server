package company.trial.service;

import javax.mail.MessagingException;

import company.trial.model.User;

public interface UserService {
    void saveUser(User user);

    boolean userExist(String email);

    boolean isUserValidated(String email);

    String generateCode();

    void sendCode(String email, String userName) throws MessagingException;

}
