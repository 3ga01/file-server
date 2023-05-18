package company.trial.controllers;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.User;
import company.trial.service.UserService;

@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView loginForm() {
        return new ModelAndView("login");
    }

    @GetMapping("/signUp")
    public ModelAndView signupForm(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("signUp");
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

    @GetMapping("/reset")
    public ModelAndView getResetPasswordPage() {
        return new ModelAndView("reset");
    }

    @PostMapping("/reset-password")
    public ModelAndView resetUserPassword(@RequestParam String email,
            @RequestParam String password, Model model) {
        boolean passwordResetSuccessful = userService.resetUserPassword(email, password);

        if (passwordResetSuccessful) {
            return new ModelAndView("login");
        } else {
            return new ModelAndView("reset");
        }
    }

}
