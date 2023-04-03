package company.trial.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Files, Long> {
  Optional<Files> findByName(String name);


  List<Files> findByNameContainingIgnoreCase(String name);
  
  
}
