package company.trial.service;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.Files;

public interface FileService {
    ResponseEntity<byte[]> getFile(String name);

    ModelAndView sendFile(String fileName, String recepEmail) throws MessagingException;

    public List<Files> searchFiles(String query);

    ResponseEntity<ByteArrayResource> downloadFile(String name);

}
