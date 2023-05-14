package company.trial.service;

import company.trial.model.User;

public interface UserService {
    void saveUser(User user);

    boolean userExist(String email);

    boolean isUserValidated(String email);

    void generateCode();
}
