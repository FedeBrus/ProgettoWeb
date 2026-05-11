package com.palestra.palestra.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping("/")
    public String homepage(Model page){
        return "public/index";
    }

    @GetMapping("/signup")
    public String signuppage(Model page) {
        return "public/auth/signup";
    }

    @GetMapping("/login")
    public String login(Model page) {
        return "public/auth/login";
    }
}
