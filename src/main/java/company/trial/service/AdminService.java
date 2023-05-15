package company.trial.service;

import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    void uploadedFile(String name, String Description, String type, MultipartFile file);
}
