package company.trial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.User;
import company.trial.service.UserDetailsServiceImpl;

@RestController
public class AdminController {

    @Autowired
    private UserDetailsServiceImpl userDetails;

    @GetMapping("/admin/login")
    public ModelAndView adminLanding(@ModelAttribute("validadmin") User user) {
        return new ModelAndView("adminLanding");

    }

}
