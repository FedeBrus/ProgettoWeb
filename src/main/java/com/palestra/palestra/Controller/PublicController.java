package com.palestra.palestra.Controller;

import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.Services.Auth.CheckUser;
import com.palestra.palestra.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Controller
public class PublicController {
    private final UserRepository userRepository;
    private final CheckUser checkUserService;

    @Autowired
    PublicController(UserRepository userRepository, CheckUser checkUserService) {
        this.userRepository = userRepository;
        this.checkUserService = checkUserService;
    }

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

    @PostMapping("/signup")
    public String addUser(
        @RequestParam String name,
        @RequestParam String surname,
        @RequestParam String email,
        @RequestParam String username,
        @RequestParam String password,
        @RequestParam String dob,
        @RequestParam String role,
        Model model
    ) {
        String returnPage;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dob);
            User newUser = new User(name, surname, email, username, password, date, role);

            if (checkUserService.checkAndInsert(newUser)) {
                returnPage = "public/auth/signup_success";
            } else {
                model.addAttribute("unsuccessful", true);
                returnPage = "public/auth/signup";
            }
        } catch (ParseException e) {
            // Come fallback in caso la data abbia un formato non valido
            returnPage = "public/auth/signup";
        }

        return returnPage;
    }
}
