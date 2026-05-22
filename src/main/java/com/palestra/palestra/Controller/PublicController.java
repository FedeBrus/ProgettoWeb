package com.palestra.palestra.Controller;

import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.Services.Auth.CheckSignUpInfo;
import com.palestra.palestra.Services.Auth.CheckUser;
import com.palestra.palestra.pojo.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Controller
public class PublicController {
    private final UserRepository userRepository;
    private final CheckUser checkUserService;
    private final CheckSignUpInfo checkSignUpInfoService;

    @Autowired
    PublicController(UserRepository userRepository, CheckUser checkUserService, CheckSignUpInfo checkSignUpInfoService) {
        this.userRepository = userRepository;
        this.checkUserService = checkUserService;
        this.checkSignUpInfoService = checkSignUpInfoService;
    }

    @GetMapping("/")
    public String homepage(Model page){
        return "public/index";
    }

    @GetMapping("/login")
    public String login(Model page) {
        return "public/auth/login";
    }

    @GetMapping("/signup")
    public String signupGet(Model page) {
        return "public/auth/signup";
    }

    @PostMapping("/signup")
    public String signupPost(
        @RequestParam String name,
        @RequestParam String surname,
        @RequestParam String email,
        @RequestParam String username,
        @RequestParam String password,
        @RequestParam String dob,
        @RequestParam String role,
        Model model
    ) {
        String returnPage = "public/auth/signup";
        try {
            LocalDate date = LocalDate.parse(dob);
            User newUser = new User(name, surname, email, username, password, date, role);

            if (checkSignUpInfoService.checkUserInfo(newUser)) {
                if (checkUserService.checkAndInsert(newUser)) {
                    returnPage = "public/auth/signup_success";
                } else {
                    model.addAttribute("failed", true);
                }
            }
        } catch (DateTimeParseException e) {
            // Come fallback in caso la data abbia un formato non valido
            returnPage = "public/auth/signup";
        }

        return returnPage;
    }

    @GetMapping("/contact")
    public String contactGet(Model page) { return "public/contact"; }

    @PostMapping("/contact")
    public String contactPost(Model page) {
        page.addAttribute("success", true);
        return "public/contact";
    }

    @PostMapping("/loginFailure")
    public String loginFailure(Model page) {
        page.addAttribute("failed", true);
        return "public/auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "public/logout";
    }
}
