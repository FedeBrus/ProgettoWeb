package com.palestra.palestra.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PublicController {

    @GetMapping("/")
    public String homepage(Model page){
        return "public/index";
    }

    @GetMapping("/login")
    public String login(Model page) {
        return "public/auth/login";
    }

    @GetMapping("/signup")
    public String signup(Model page) {
        return "public/auth/signup";
    }

    @GetMapping("/contact")
    public String contactGet(Model page) { return "public/contact"; }

    @PostMapping("/contact")
    public String contactPost(Model page) {
        page.addAttribute("success", true);
        return "public/contact";
    }
}
