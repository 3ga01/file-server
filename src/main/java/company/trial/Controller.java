package company.trial;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
  public ModelAndView login(@ModelAttribute("validusers") User user, Model model) {
    User foundUser = userRepository.findByEmail(user.getEmail());
    if (foundUser != null && foundUser.getPassword().equals(user.getPassword()) && foundUser.isVerified() == true) {

      List<Files> files = fileRepository.findAll();
      model.addAttribute("files", files);
      return new ModelAndView("landing");

    } else {
      return new ModelAndView("login");
    }

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
      @RequestParam("description") String description) {
    try {
      Files uploadedFile = new Files();
      uploadedFile.setName(name);
      uploadedFile.setDescription(description);
      uploadedFile.setFiles(file.getBytes());

      // Save the file to the database
      fileRepository.save(uploadedFile);

      return new ModelAndView("adminLanding");
    } catch (IOException e) {
      // handle the exception
      return new ModelAndView("uploadFile");

    }
  }

}
