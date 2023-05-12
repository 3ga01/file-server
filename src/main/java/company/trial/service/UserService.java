package company.trial.service;

import company.trial.model.User;

public interface UserService {
    void saveUser(User user);

    User getUserByEmail(String email);
}
