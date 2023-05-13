package company.trial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return new ModelAndView("signUp");
        }
       if (userService.userExist(user.getEmail())){
           return new ModelAndView("signUp");
       }
        userService.saveUser(user);
        return new ModelAndView("verify");
    }

}
