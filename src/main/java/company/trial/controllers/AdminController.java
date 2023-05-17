package company.trial.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import company.trial.model.Files;
import company.trial.model.User;
import company.trial.repositories.FileRepository;
import company.trial.service.AdminService;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/admin/login")
    public ModelAndView adminLanding(@ModelAttribute("validadmin") User user) {
        return new ModelAndView("adminLanding");

    }

    @GetMapping("/admin/upload")
    public ModelAndView uploadPage() {
        return new ModelAndView("uploadFile");

    }

    @PostMapping("/admin/upload")
    public ModelAndView handleFileUpload(@RequestParam("files") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description, @RequestParam("type") String type) throws IOException {

        adminService.uploadedFile(name, description, type, file);
        return new ModelAndView("redirect:/admin/login");

    }

    @GetMapping("/admin/viewFiles")
    public ModelAndView listProducts(Model model) {
        List<Files> files = fileRepository.findAll();
        model.addAttribute("files", files);
        return new ModelAndView("viewFiles");
    }

}
