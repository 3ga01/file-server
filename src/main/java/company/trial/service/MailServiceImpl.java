package company.trial.service;

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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import company.trial.repositories.FileRepository;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FileRepository fileRepository;

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
    public void sendEmailWithAttachment(String toEmail, String fileName, byte[] fileData, String fileType)
            throws MessagingException {
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

}
