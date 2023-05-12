package company.trial.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import company.trial.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
  /**
   * @param email email of the admin
   * @return
   */
  Admin findByEmail(String email);

}
