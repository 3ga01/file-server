package company.trial.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ModelAndView signupSubmit(@ModelAttribute("user") User user,
    BindingResult result, Model model) {
    if (result.hasErrors()) {
    return new ModelAndView("signUp");
    }
    if (userService.isUserAlreadyPresent(user.getEmail())) {
    model.addAttribute("errorMessage", "This email is already registered!");
    return new ModelAndView("signUp");

    }
    userService.saveUser(user);
    model.addAttribute("successMessage", "User registration successful!");
    return new ModelAndView("new");
    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        return new ModelAndView("login");
    }

    @GetMapping("/user/lgin")
    public ModelAndView welcome() {
        return new ModelAndView("new");
    }

}
