package company.trial.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Files, Long> {
  Files findByName(String name);

  List<Files> findByNameContainingIgnoreCase(String name);
  
  
}
