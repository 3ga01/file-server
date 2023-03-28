package company.trial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import company.trial.repositories.UserRepository;

//import company.trial.User.UserController;

@SpringBootApplication
@ComponentScan(basePackages = "company.trial.repositories")
@EnableJpaRepositories(basePackages = "company.trial.repositories")
@ComponentScan(basePackageClasses = UserRepository.class)

public class TrialApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrialApplication.class, args);
	}

}

// @ComponentScan(basePackages = { "company.trial.AdminReporsitory" })
// @ComponentScan(basePackages = { "company.trial.UserReporsitory" })
// @ComponentScan(basePackageClasses = Controller.class)
// @ComponentScan({"company.*"})
// @EnableJpaRepositories("company.trial.*")