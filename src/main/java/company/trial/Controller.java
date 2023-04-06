package company.trial;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
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
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import company.trial.repositories.Admin;
import company.trial.repositories.AdminRepository;
import company.trial.repositories.FileRepository;
import company.trial.repositories.Files;
import company.trial.repositories.User;
import company.trial.repositories.UserRepository;

@RestController
public class Controller {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AdminRepository adminRepository;

  @Autowired
  private FileRepository fileRepository;

  @Autowired
  private JavaMailSender mailSender;

  // show Hom Page
  @GetMapping("/")
  public ModelAndView showHome() {
    return new ModelAndView("index");
  }

  // verify user
  @PostMapping("/verify")
  public ModelAndView verifyUser(@ModelAttribute("verify") User user, Model model) {
    user = userRepository.findByVerificationCode(user.getVerificationCode());
    if (user != null) {
      user.setVerified(true);
      userRepository.save(user);

      return new ModelAndView("welcome");
    }

    else {
      return new ModelAndView("verify");

    }

  }

  // add new users
  @PostMapping("/adduser")
  public ModelAndView saveUser(@ModelAttribute("users") User user) throws MessagingException {
    String userNme = user.getName();
    String verificationCode = generateVerificationCode();
    user.setVerificationCode(verificationCode);
    user.setVerified(false);
    user.setVerificationCode(verificationCode);
    userRepository.save(user);
    sendVerificationEmail(user.getEmail(), verificationCode, userNme);
    return new ModelAndView("verify");
  }

  private String generateVerificationCode() {
    // Generate a random 6-digit code
    return String.format("%06d", new Random().nextInt(999999));
  }

  private void sendVerificationEmail(String email, String verificationCode, String userName) throws MessagingException {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Verify your file sever account");
    message.setText("Dear " + userName + "\n" + "\n Welcome to file server! We're excited to have you join us.\n"
        + "\nTo complete your account setup, please use the verification code below: \n" + "\nVerification Code: "
        + verificationCode + "\n"
        + "\nPlease enter this code on the verification page to confirm your account and start using our service.\n"
        + "\nIf you didn't sign up for an account with us, please ignore this message. Someone may have used your email address by mistake, and no further action is required. \n"
        + "\nIf you have any questions or need assistance with your account, please contact our support team at emmanuel.omari@amalitech.org/+233 591 961 186.\n"
        + "\nThank you for choosing file Server. We look forward to serving you!\n" + "\nBest regards,\n"
        + "File Server team");
    mailSender.send(message);
  }

  // get signUp Page
  @GetMapping("/signUp")
  public ModelAndView showPage() {
    return new ModelAndView("signUp");
  }

  // proceed to landing page after user signs up
  @PostMapping("/userPage")
  public ModelAndView newSignUp(Model model) {
    List<Files> files = fileRepository.findAll();
    model.addAttribute("files", files);
    return new ModelAndView("landing");
  }

  // get login page on request
  @GetMapping("/login")
  public ModelAndView loginPage(Model model) {
    model.addAttribute("validusers", new User());
    return new ModelAndView("login");
  }

  // validate user on login
  @PostMapping("/login")
  public ModelAndView login(@ModelAttribute("validusers") User user, Model model) throws MessagingException {
    User foundUser = userRepository.findByEmail(user.getEmail());
    String userName = foundUser.getName();
    if (foundUser != null && foundUser.getPassword().equals(user.getPassword()) && foundUser.isVerified() == true) {
      sendLoginAlert(foundUser.getEmail(), userName);
      List<Files> files = fileRepository.findAll();
      model.addAttribute("files", files);
      return new ModelAndView("landing");

    } else {
      return new ModelAndView("login");
    }

  }

  private void sendLoginAlert(String email, String userName) throws MessagingException {
    LocalDateTime currentDateTime = LocalDateTime.now();
    Locale locale = Locale.getDefault();
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("File Server Account Login!!!");
    message.setText("Dear " + userName + "\n"
        + "\nWe wanted to let you know that someone recently signed in to your file server account on "
        + currentDateTime
        + " from an unknown device or location.\n"
        + "\nIf this was you, then you can disregard this message. However, if you did not sign in or you believe someone else may have accessed your account, please take immediate action to secure your account. This includes changing your password and reviewing your account activity.\n"
        + "\nThank you for helping us keep your account safe.\n" + "\nBest regards,\n" + "File Server Team");
    mailSender.send(message);
  }

  // get reset user password page
  @GetMapping("/reset")
  public ModelAndView showResetPasswordForm() {
    return new ModelAndView("reset");
  }

  // validate and update user password
  @PostMapping("/reset-password")
  public ModelAndView resetUserPassword(@RequestParam String email,
      @RequestParam String password, Model model) {
    User user = userRepository.findByEmail(email);
    if (user != null && user.isVerified() == true) {
      user.setPassword(password);
      userRepository.save(user);
      return new ModelAndView("login");
    } else {
      return new ModelAndView("reset");

    }
  }

  // Admin
  @GetMapping("/adminLogin")
  public ModelAndView showAdminLogin() {
    return new ModelAndView("adminLogin");
  }

  @PostMapping("/adminLogin")
  public ModelAndView adminLogin(@ModelAttribute("validadmin") Admin admin) {
    Admin foundAdmin = adminRepository.findByEmail(admin.getEmail());
    if (foundAdmin != null &&
        foundAdmin.getPassword().equals(admin.getPassword())) {
      return new ModelAndView("adminLanding");
    } else {
      return new ModelAndView("adminLogin");
    }

  }

  @GetMapping("/adminReset")
  public ModelAndView showAdminReset() {
    return new ModelAndView("adminReset");
  }

  @PostMapping("/resetAdmin-password")
  public ModelAndView resetAdminPasswordAndView(@RequestParam String email,
      @RequestParam String password,
      Model model) {
    Admin admin = adminRepository.findByEmail(email);
    if (admin != null) {
      admin.setPassword(password);
      adminRepository.save(admin);
      return new ModelAndView("adminLogin");
    } else {
      return new ModelAndView("adminReset");
    }
  }

  // upload page
  @PostMapping("/uploadPage")
  public ModelAndView showUploadPage() {
    return new ModelAndView("uploadFile");
  }

  // upload
  @PostMapping("/upload")
  public ModelAndView handleFileUpload(@RequestParam("files") MultipartFile file,
      @RequestParam("name") String name,
      @RequestParam("description") String description, @RequestParam("type") String type) {
    try {
      Files uploadedFile = new Files();
      uploadedFile.setName(name);
      uploadedFile.setDescription(description);
      uploadedFile.setFiles(file.getBytes());
      uploadedFile.setType(type);
      uploadedFile.setDownloadCount(0);
      uploadedFile.setMailCount(0);

      // Save the file to the database
      fileRepository.save(uploadedFile);

      return new ModelAndView("adminLanding");
    } catch (IOException e) {
      // handle the exception
      return new ModelAndView("uploadFile");

    }
  }

  // search
  @PostMapping("/search")
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

  // preview
  @GetMapping("/preview/{name:.+}")
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

  // dowload files
  @GetMapping("/download/{name:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String name) {
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

  // send email
  @PostMapping("/send")
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

  // view files
  @PostMapping("/view")
  public ModelAndView listProducts(Model model) {
    List<Files> files = fileRepository.findAll();
    model.addAttribute("files", files);
    return new ModelAndView("viewFiles");
  }

}
