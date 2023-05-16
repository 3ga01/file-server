package company.trial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
