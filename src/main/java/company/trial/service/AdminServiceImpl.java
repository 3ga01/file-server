package company.trial.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import company.trial.model.Files;
import company.trial.repositories.FileRepository;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private FileRepository fileRepository;

    @Override
    public void uploadedFile(String name, String description, String type, MultipartFile file)  {
        

            Files uploadedFile = new Files();
            uploadedFile.setName(name);
            uploadedFile.setDescription(description);
            try {
                uploadedFile.setFiles(file.getBytes());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            uploadedFile.setType(type);
            uploadedFile.setDownloadCount(0);
            uploadedFile.setMailCount(0);

            // Save the file to the database
            fileRepository.save(uploadedFile);

           
    }
    

}
