package company.trial.controllers;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.Files;
import company.trial.model.User;
import company.trial.repositories.FileRepository;
import company.trial.service.FileService;
import company.trial.service.MailService;
import company.trial.service.UserService;

@RestController
public class UserController {

    @Autowired
    private FileService fileService;

    @Autowired
    MailService mailService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("login");
    }

    @GetMapping("/user/verify")
    public ModelAndView showUserVerificatinPage() {
        return new ModelAndView("verify");
    }

    @PostMapping("/user/verify")
    public ModelAndView verifyUser(@ModelAttribute("verify") User user, Model model) {

        if (userService.verify(user)) {
            return new ModelAndView("redirect:/user/landing");
        }

        model.addAttribute("message", "Verification Failed!!!...Try Again");
        return new ModelAndView("verify");
    }

    @GetMapping("/user/preview/{name:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable String name) {

        return fileService.getFile(name);

    }

    // @Secured("ROLE_USER")
    @GetMapping("/user/landing")
    public ModelAndView newSignUp(Model model) {
        List<Files> files = fileRepository.findAll();
        model.addAttribute("files", files);
        return new ModelAndView("landing");
    }

    @GetMapping("/user/download/{name:.+}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String name) {
        return fileService.downloadFile(name);
    }

    @PostMapping("/user/search")
    public ModelAndView search(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Files> files = fileService.searchFiles(query);
        model.addAttribute("files", files);
        return new ModelAndView("landing");
    }

    @PostMapping("/user/send")
    public ModelAndView sendFile(@RequestParam("name") String fileName,
            @RequestParam("recepEmail") String recepEmail) throws MessagingException {

        return fileService.sendFile(fileName, recepEmail);

    }

}
