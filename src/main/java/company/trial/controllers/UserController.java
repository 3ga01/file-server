package company.trial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.User;
import company.trial.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signUp")
    public ModelAndView signupForm(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("signUp");
    }

    @PostMapping("/signUp")
    public ModelAndView signupSubmit(@ModelAttribute User user) {
        userService.save(user);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        return new ModelAndView("login");
    }

}
