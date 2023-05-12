package company.trial.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import company.trial.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);

  User findByVerificationCode(String verificationCode);

  User findByName(String name);

}
