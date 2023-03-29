package company.trial;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import company.trial.repositories.Admin;
import company.trial.repositories.AdminRepository;
import company.trial.repositories.User;
import company.trial.repositories.UserRepository;

@RestController
public class Controller {

  @Autowired
  private UserRepository userRepository;

   @Autowired 
   private AdminRepository adminRepository;

  // show Hom Page
  @GetMapping("/")
  public ModelAndView showHome() {
    return new ModelAndView("index");
  }

  // add new users
  @PostMapping("/adduser")
  public ModelAndView saveUser(@ModelAttribute("users") User user) {
    userRepository.save(user);
    return new ModelAndView("welcome");
  }

  // get signUp Page
  @GetMapping("/signUp")
  public ModelAndView showPage() {
    return new ModelAndView("signUp");
  }

   // proceed to landing page after user signs up
  @PostMapping("/userPage")
  public ModelAndView newSignUp() {
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
  public ModelAndView login(@ModelAttribute("validusers") User user) {
    User foundUser = userRepository.findByEmail(user.getEmail());
    if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
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
  if (user != null) {
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

  // @PostMapping("/resetAdmin-password")
  // public ModelAndView resetAdminPasswordAndView(@RequestParam String email,
  // @RequestParam String password,
  // Model model) {
  // Admin admin = adminRepository.findByemail(email);
  // if (admin != null) {
  // admin.setPassword(password);
  // adminRepository.save(admin);
  // return new ModelAndView("adminLogin");
  // } else {
  // return new ModelAndView("adminReset");
  // }
  // }

}
