package company.trial.service;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.Files;
import company.trial.repositories.FileRepository;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MailService mailService;

    @Override
    public List<Files> searchFiles(String query) {
        if (query == null) {
            return fileRepository.findAll();
        } else {
            return fileRepository.findByNameContainingIgnoreCase(query);
        }
    }

    @Override
    public ModelAndView sendFile(String fileName, String recepEmail) throws MessagingException {
        Optional<Files> fileOptional = fileRepository.findByName(fileName);

        if (fileOptional.isPresent()) {
            Files file = fileOptional.get();
            String fileType = file.getType();

            mailService.sendEmailWithAttachment(recepEmail, fileName, file.getFiles(), fileType);
            file.setMailCount(file.getMailCount() + 1);
            fileRepository.save(file);
            return new ModelAndView("landing");
        } else {
            return new ModelAndView("landing");
        }
    }

    @Override
    public ResponseEntity<byte[]> getFile(String name) {
        Optional<Files> fileOptional = fileRepository.findByName(name);

        if (!fileOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Files file = fileOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getType()));
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(file.getName()).build());

        return new ResponseEntity<>(file.getFiles(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadFile(String name) {
        Optional<Files> optionalFile = fileRepository.findByName(name);

        if (optionalFile.isPresent()) {
            Files file = optionalFile.get();
            String fileType = file.getType();

            file.setDownloadCount(file.getDownloadCount() + 1);
            fileRepository.save(file);

            // Create a ByteArrayResource from the file content
            ByteArrayResource resource = new ByteArrayResource(file.getFiles());

            // Set the response headers to indicate a file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; name=" + file.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, fileType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } else {
            // Handle the case where the file is not found
            return ResponseEntity.notFound().build();
        }
    }

}
