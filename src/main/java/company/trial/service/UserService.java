package company.trial.service;

import javax.mail.MessagingException;

import company.trial.model.User;

public interface UserService {
    void saveUser(User user) throws MessagingException;

    boolean userExist(String email);

    String generateCode();

    boolean verify(User user);

    boolean resetUserPassword(String email, String password);


}
