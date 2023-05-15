package company.trial.service;

import javax.mail.MessagingException;

public interface MailService {
    void sendCode(String email, String userName, String code) throws MessagingException;

    void sendEmailWithAttachment(String toEmail, String fileName, byte[] fileData, String fileType)
            throws MessagingException;

   
}
