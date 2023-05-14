package company.trial.controllers;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.User;
import company.trial.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/signUp")
    public ModelAndView signupSubmit(@ModelAttribute("user") User user,
            BindingResult result, Model model) throws MessagingException {

        if (result.hasErrors()) {
            model.addAttribute("message", "SignUp Failed!!!...Try Again");

            return new ModelAndView("signUp");
        }

        if (userService.userExist(user.getEmail())) {
            model.addAttribute("null", "SignUp Failed!!!...Email: " + user.getEmail() + " already exist");

            return new ModelAndView("signUp");
        }

        userService.sendCode(user.getEmail(), user.getName());

        userService.saveUser(user);
        return new ModelAndView("login");
    }

}
