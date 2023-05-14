package company.trial.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.User;

 @RestController
public class AdminController {

    @GetMapping("/admin/login")
    public ModelAndView adminLogin(@ModelAttribute("validadmin") User user) {
        return new ModelAndView("adminLanding");

    }

}
