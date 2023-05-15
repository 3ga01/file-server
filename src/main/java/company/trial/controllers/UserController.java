package company.trial.controllers;

import java.util.List;
import java.util.Optional;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.Files;
import company.trial.model.User;
import company.trial.repositories.FileRepository;
import company.trial.service.UserDetailsServiceImpl;
import company.trial.service.UserService;

@RestController
public class UserController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetails;

    @PostMapping("/login")
    public ModelAndView userLogin(@ModelAttribute("user") User user) {
        userDetails.loadUserByUsername("user.getEmai()");
        return new ModelAndView("redirect:/user/landing");
    }

    @GetMapping("/user/login")
    public ModelAndView getLanding() {

        return new ModelAndView("redirect:/user/landing");
    }

    @PostMapping("/signUp")
    public ModelAndView signupSubmit(@ModelAttribute("user") User user,
            BindingResult result, Model model) throws MessagingException {

        if (result.hasErrors()) {
            model.addAttribute("message", "SignUp Failed!!!...Try Again");

            return new ModelAndView("signUp");
        }

        if (userService.userExist(user.getEmail())) {
            model.addAttribute("message", "SignUp Failed!!!...Email: " + user.getEmail() + " already exist");

            return new ModelAndView("signUp");
        }

        userService.saveUser(user);
        return new ModelAndView("redirect:/user/verify");
    }

    @GetMapping("/user/verify")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView showUserVerificatinPage() {
        return new ModelAndView("verify");
    }

    @PostMapping("/user/verify")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView verifyUser(@ModelAttribute("verify") User user, Model model) {

        if (userService.verify(user)) {
            return new ModelAndView("redirect:/user/landing");

        }

        model.addAttribute("message", "Verification Failed!!!...Try Again");
        return new ModelAndView("verify");
    }

    @GetMapping("/user/preview/{name:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable String name) {

        Optional<Files> fileOptional = fileRepository.findByName(name);
        if (!fileOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Files files = fileOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(files.getType()));
        // headers.setContentLength(files.getFiles().length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(files.getName()).build());
        return new ResponseEntity<>(files.getFiles(), headers, HttpStatus.OK);

    }

    @GetMapping("/user/landing")
    public ModelAndView newSignUp(Model model) {
        List<Files> files = fileRepository.findAll();
        model.addAttribute("files", files);
        return new ModelAndView("landing");
    }

    @GetMapping("/user/download/{name:.+}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String name) {
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
            headers.add(HttpHeaders.CONTENT_TYPE, fileType); // Add fileType to the response headers

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    // .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            // Handle the case where the file is not found
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/search")
    public ModelAndView search(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Files> files;
        if (query == null) {
            files = fileRepository.findAll();
        } else {
            files = fileRepository.findByNameContainingIgnoreCase(query);
        }
        model.addAttribute("files", files);
        return new ModelAndView("landing");
    }

    @PostMapping("/user/send")
    public ModelAndView sendFile(@RequestParam("name") String fileName,
            @RequestParam("recepEmail") String recepEmail) throws MessagingException {

        Optional<Files> fileOptional = fileRepository.findByName(fileName);

        if (fileOptional.isPresent()) {
            Files file = fileOptional.get();
            String fileType = file.getType();

            sendEmailWithAttachment(recepEmail, fileName, file.getFiles(), fileType);
            file.setMailCount(file.getMailCount() + 1);
            fileRepository.save(file);
            return new ModelAndView("landing");

            // code to send the file as an email attachment to the recipient
        } else {

            return new ModelAndView("landing");
        }

    }

    private void sendEmailWithAttachment(String toEmail, String fileName, byte[] fileData, String fileType)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        // Set the To address
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

        // Set the subject
        message.setSubject("File attachment: " + fileName);

        // Create the message body
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Please find attached file.");

        // Create the attachment
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        ByteArrayDataSource dataSource = new ByteArrayDataSource(fileData, fileType);
        attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
        attachmentBodyPart.setFileName(fileName);

        // Add the message body and attachment to the multipart message
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentBodyPart);
        message.setContent(multipart);

        // Send the message
        mailSender.send(message);
    }

}
