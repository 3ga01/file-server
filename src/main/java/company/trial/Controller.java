package company.trial;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
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
    String verificationCode = generateVerificationCode();
    user.setVerificationCode(verificationCode);
    user.setVerified(false);
    user.setVerificationCode(verificationCode);
    userRepository.save(user);
    sendVerificationEmail(user.getEmail(), verificationCode);
    return new ModelAndView("verify");
  }

  private String generateVerificationCode() {
    // Generate a random 6-digit code
    return String.format("%06d", new Random().nextInt(999999));
  }

  private void sendVerificationEmail(String email, String verificationCode) throws MessagingException {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Verify your account");
    message.setText("Your verification code is: " + verificationCode);
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
    if (foundUser != null && foundUser.getPassword().equals(user.getPassword()) && foundUser.isVerified() == true) {
      sendLoginAlert(foundUser.getEmail());
      List<Files> files = fileRepository.findAll();
      model.addAttribute("files", files);
      return new ModelAndView("landing");

    } else {
      return new ModelAndView("login");
    }

  }

  private void sendLoginAlert(String email) throws MessagingException {
    LocalDateTime currentDateTime = LocalDateTime.now();
    Locale locale = Locale.getDefault();
    String countryName = locale.getDisplayCountry();
    String osName = System.getProperty("os.name");
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Account Login!!!");
    message.setText(
        "New login at " + currentDateTime + " by " + osName + " device from " + " " + countryName + "\n"
            + "if this wasn't authorized by you,  please click on the link to reset your password.");
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
      @RequestParam("description") String 
    description, @RequestParam("type") String type) {
    try {
      Files uploadedFile = new Files();
      uploadedFile.setName(name);
      uploadedFile.setDescription(description);
      uploadedFile.setFiles(file.getBytes());
      uploadedFile.setType(type);

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


  //preview
  @GetMapping("/preview/{name:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable String name) {
    Optional<Files> fileOptional = fileRepository.findByName(name);
    if (!fileOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    Files files = fileOptional.get();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(files.getType()));
    //headers.setContentLength(files.getFiles().length);
    headers.setContentDisposition(ContentDisposition.builder("inline").filename(files.getName()).build());
    return new ResponseEntity<>(files.getFiles(), headers, HttpStatus.OK);
  }

}
