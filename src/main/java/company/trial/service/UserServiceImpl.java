package company.trial.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.activation.DataHandler;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import company.trial.model.Files;
import company.trial.model.Role;
import company.trial.model.User;
import company.trial.repositories.FileRepository;
import company.trial.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator userValidator;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public void saveUser(User user) throws MessagingException {
        
        String code = generateCode();
        Errors errors = new BeanPropertyBindingResult(user, "user");
        ValidationUtils.invokeValidator(userValidator, user, errors);

        if (errors.hasErrors()) {

        }
        sendCode(user.getEmail(), user.getName(), code);

        user.setVerified(false);

        user.setVerificationCode(code);

        user.setRoles(Collections.singleton(Role.USER)); 

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public boolean userExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserValidated(String email) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Override
    public void sendCode(String email, String userName, String code) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify your file sever account");
        message.setText("Dear " + userName + "\n" + "\n Welcome to file server! We're excited to have you join us.\n"
                + "\nTo complete your account setup, please use the verification code below: \n"
                + "\nVerification Code: "
                + code + "\n"
                + "\nPlease enter this code on the verification page to confirm your account and start using our service.\n"
                + "\nIf you didn't sign up for an account with us, please ignore this message. Someone may have used your email address by mistake, and no further action is required. \n"
                + "\nIf you have any questions or need assistance with your account, please contact our support team at emmanuel.omari@amalitech.org/+233 591 961 186.\n"
                + "\nThank you for choosing file Server. We look forward to serving you!\n" + "\nBest regards,\n"
                + "File Server team");
        mailSender.send(message);

    }

    @Override
    public boolean verify(User user) {

        String verificationCode = user.getVerificationCode();

        user = userRepository.findByVerificationCode(verificationCode);

        if (user != null) {
            user.setVerified(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void findAllFiles() {
        List<Files> files = fileRepository.findAll();
        
    }

    @Override
    public void findFileByEmail() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findFileByEmail'");
    }

    @Override
    public void sendEmailWithAttachment(String toEmail, String fileName, byte[] fileData, String fileType) throws MessagingException {
        // TODO Auto-generated method stub
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

    

    
}
