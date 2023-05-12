package company.trial.service;

import org.springframework.beans.factory.annotation.Autowired;

import company.trial.model.Admin;
import company.trial.repositories.AdminRepository;

public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public void save(Admin admin) {
        adminRepository.save(admin);
    }
}
