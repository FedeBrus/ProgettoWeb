package com.palestra.palestra.Controller;

import com.palestra.palestra.Services.Trial.TrialUserManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class DashboardController {
    private final TrialUserManager trialUserManager;

    @Autowired
    public DashboardController(
            TrialUserManager trialUserManager
    ) {
        this.trialUserManager = trialUserManager;
    }

    @GetMapping("/dashboard/prova")
    public String provaDashboard(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        page.addAttribute("allenamenti", trialUserManager.getTimesTrained(username));
        page.addAttribute("username", username);

        return "private/dashboard/prova";
    }

    @GetMapping("/dashboard/basic")
    public String basicDashboard(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        page.addAttribute("username", username);

        return "private/dashboard/basic";
    }

    @GetMapping("/dashboard/pro")
    public String proDashboard(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        page.addAttribute("username", username);

        return "private/dashboard/pro";
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        page.addAttribute("username", username);
        page.addAttribute("number_of_removed_users", null);
        return "private/dashboard/admin";
    }

    @GetMapping("/dashboard")
    public String dashboardHandler(Authentication auth) {
        String returnPage = "";
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_PROVA"))) {
            returnPage = "forward:/dashboard/prova";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_BASIC"))) {
            returnPage = "forward:/dashboard/basic";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_PRO"))) {
            returnPage = "forward:/dashboard/pro";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            returnPage = "forward:/dashboard/admin";
        }

        return returnPage;
    }
}
