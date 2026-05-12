package com.palestra.palestra.Controller;

import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.Services.Trial.TrialUserManager;
import com.palestra.palestra.Services.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class DashboardController{
    private final TrialUserManager trialUserManager;
    private final UserRepository repo;
    private final UserUtils utils;

    @Autowired
    public DashboardController(TrialUserManager trialUserManager, UserRepository repo, UserUtils utils) {
        this.trialUserManager = trialUserManager;
        this.repo = repo;
        this.utils = utils;
    }

    @GetMapping("/dashboard/prova")
    public String provaDashboard(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        page.addAttribute("allenamenti", trialUserManager.getTimesTrained(username));
        page.addAttribute("username", username);

        return "public/dashboard/prova";
    }

    @GetMapping("/dashboard")
    public String dashboardHandler(Authentication auth) {
        String returnPage = "";
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_PROVA"))) {
            returnPage = "forward:/dashboard/prova";
        }

        return returnPage;
    }

    @GetMapping("/dashboard/profile")
    public String userProfile(Model page, Authentication auth) {
        User authUser = ((User) Objects.requireNonNull(auth.getPrincipal()));
        com.palestra.palestra.pojo.User u = repo.getUserDetails(authUser.getUsername());
        page.addAttribute("username", authUser.getUsername());
        page.addAttribute("name", u.getName());
        page.addAttribute("surname", u.getSurname());
        page.addAttribute("dob", u.getDate_of_birth());
        page.addAttribute("role", authUser.getAuthorities().iterator().next().toString());

        return "public/dashboard/view_profile";
    }

    @GetMapping("/dashboard/change_password")
    public String changePassword(Model page, Authentication auth) {
        return "public/dashboard/change_password";
    }

    @PostMapping("/dashboard/change_password")
    public String changePasswordPost(Model page,
                                     Authentication auth,
                                     @RequestParam String password) {
        User u = (User) auth.getPrincipal();
        if(utils.changePassword(Objects.requireNonNull(u).getUsername(), password)) {
            page.addAttribute("success", true);
        } else {
            page.addAttribute("success", false);
        }
        return "public/dashboard/change_password";
    }
}
