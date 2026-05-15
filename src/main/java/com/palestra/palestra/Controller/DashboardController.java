package com.palestra.palestra.Controller;

import com.palestra.palestra.OpenFeignClients.TrainingAPIClient;
import com.palestra.palestra.Repositories.CustomExerciseRepository;
import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.Services.ProgramInserterService;
import com.palestra.palestra.Services.Trial.TrialUserManager;
import com.palestra.palestra.Services.UserUtils;
import com.palestra.palestra.pojo.Exercise;
import com.palestra.palestra.pojo.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

@Controller
public class DashboardController {
    private final TrialUserManager trialUserManager;
    private final UserRepository repo;
    private final UserUtils utils;
    private final TrainingAPIClient trainingClient;
    private final ProgramInserterService programInserter;

    @Autowired
    public DashboardController(TrialUserManager trialUserManager, UserRepository repo, UserUtils utils, TrainingAPIClient trainingClient, ProgramInserterService programInserter) {
        this.trialUserManager = trialUserManager;
        this.repo = repo;
        this.utils = utils;
        this.trainingClient = trainingClient;
        this.programInserter = programInserter;
    }

    @GetMapping("/dashboard/prova")
    public String provaDashboard(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        page.addAttribute("allenamenti", trialUserManager.getTimesTrained(username));
        page.addAttribute("username", username);

        return "public/dashboard/prova";
    }

    @GetMapping("/dashboard/pro")
    public String proDashboard(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        page.addAttribute("username", username);

        return "public/dashboard/pro";
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        page.addAttribute("username", username);
        page.addAttribute("number_of_removed_users", null);
        return "public/dashboard/admin";
    }

    @GetMapping("/dashboard")
    public String dashboardHandler(Authentication auth) {
        String returnPage = "";
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_PROVA"))) {
            returnPage = "forward:/dashboard/prova";
        } else if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_PRO"))) {
            returnPage = "forward:/dashboard/pro";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            returnPage = "forward:/dashboard/admin";
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

    @GetMapping("/dashboard/user_list")
    public String userList(Model page) {
        List<com.palestra.palestra.pojo.User> allUsers = repo.getAllUserDetails();
        page.addAttribute("users", allUsers);

        return "public/dashboard/user_list";
    }

    @GetMapping("/dashboard/remove_expired_users")
    public String removeExpiredUsers(Model page, Authentication auth) {
        User authUser = ((User) Objects.requireNonNull(auth.getPrincipal()));
        int numberOfRemovedUsers = repo.removeExpiredUsers();
        page.addAttribute("number_of_removed_users", numberOfRemovedUsers);
        page.addAttribute("username", authUser.getUsername());
        return "public/dashboard/admin";
    }
  
    @GetMapping("/dashboard/upgrade")
    public String upgradeProfile(Model page, Authentication auth) {
        page.addAttribute("role", auth.getAuthorities().iterator().next().toString().replace("ROLE_", ""));
        return "public/dashboard/upgrade";
    }

    @GetMapping("/dashboard/training")
    public String defaultPrograms(Model page, Authentication auth) {
        List<Program> defaultPrograms = trainingClient.getDefaultPrograms();
        page.addAttribute("defaultPrograms", defaultPrograms);
        return "public/dashboard/training";
    }

    public String updateProfile(Model page, Authentication auth, String newRole) {
        User u = (User) Objects.requireNonNull(auth.getPrincipal());
        try {
            auth = utils.changeRole(u.getUsername(), new SimpleGrantedAuthority(newRole), auth);
            page.addAttribute("success", true);
        } catch (Exception e) {
            page.addAttribute("success", false);
        }

        page.addAttribute("role", auth.getAuthorities().iterator().next().toString().replace("ROLE_", ""));
        return "public/dashboard/upgrade";
    }

    @PostMapping("/dashboard/upgrade/basic")
    public String upgradeProfileBasic(Model page, Authentication auth) {
        return updateProfile(page, auth, "ROLE_USER_BASIC");
    }

    @PostMapping("/dashboard/upgrade/pro")
    public String upgradeProfilePro(Model page, Authentication auth) {
        return updateProfile(page, auth, "ROLE_USER_PRO");
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

    @GetMapping("/dashboard/review")
    public String reviewForm() {
        return "public/dashboard/review";
    }

    @GetMapping("/dashboard/insert_program")
    public String insertProgramForm() {
        return "public/dashboard/insert_program";
    }

    @PostMapping("/dashboard/insert_program")
    public String insertProgramInDB(
            @RequestParam String programName,
            @RequestParam String exercises,
            Model page
    ) {
        boolean success = true;
        try {
            int calories = programInserter.addProgramToDB(programName, exercises);
            page.addAttribute("calories", calories);
        } catch (JacksonException e) {
            success = false;
            page.addAttribute("reason", "L'input inviato non è valido!");
        } catch (IllegalStateException e) {
            success = false;
            page.addAttribute("reason", "Esiste già un programma con quel nome!");
        }
        page.addAttribute("success", success);
        return "public/dashboard/insert_program";
    }
}
