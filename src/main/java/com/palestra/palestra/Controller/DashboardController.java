package com.palestra.palestra.Controller;

import com.palestra.palestra.Services.Trial.TrialUserChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class DashboardController{
    private final TrialUserChecker trialUserChecker;

    @Autowired
    public DashboardController(TrialUserChecker trialUserChecker) {
        this.trialUserChecker = trialUserChecker;
    }

    @GetMapping("/dashboard/prova")
    public String provaDashboard(Model page, Authentication auth) {
        page.addAttribute("allenamenti", trialUserChecker.getTimesTrained(
                ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername())
        );
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
}
