package company.trial.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.User;

@RestController
public class MainController {

    @GetMapping("/login")
    public ModelAndView loginForm() {
        return new ModelAndView("login");
    }

    @GetMapping("/signUp")
    public ModelAndView signupForm(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("signUp");
    }

}
