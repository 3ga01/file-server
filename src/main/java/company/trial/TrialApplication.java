package company.trial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import company.trial.repositories.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = "company.trial.repositories")
@EnableJpaRepositories(basePackages = "company.trial.repositories")
@ComponentScan(basePackageClasses = UserRepository.class)

public class TrialApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrialApplication.class, args);
	}

}
