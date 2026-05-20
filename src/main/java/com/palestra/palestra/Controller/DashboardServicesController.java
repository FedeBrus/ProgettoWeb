package com.palestra.palestra.Controller;

import com.palestra.palestra.Repositories.StatisticsRepository;
import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.Services.CustomProgramService;
import com.palestra.palestra.Services.ProgramService;
import com.palestra.palestra.Services.Trial.TrialUserManager;
import com.palestra.palestra.Services.UserUtils;
import com.palestra.palestra.pojo.Exercise;
import com.palestra.palestra.pojo.GlobalStatEntry;
import com.palestra.palestra.pojo.PersonalStatEntry;
import com.palestra.palestra.pojo.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tools.jackson.core.JacksonException;

import java.util.List;
import java.util.Objects;

@Controller
public class DashboardServicesController {
    private final UserRepository repo;
    private final UserUtils utils;
    private final ProgramService programService;
    private final CustomProgramService customPrograms;
    private final StatisticsRepository statisticsRepository;

    @Autowired
    public DashboardServicesController(
            UserRepository repo,
            UserUtils utils,
            ProgramService programService,
            CustomProgramService customPrograms,
            StatisticsRepository statisticsRepository
    ) {
        this.repo = repo;
        this.utils = utils;
        this.programService = programService;
        this.customPrograms = customPrograms;
        this.statisticsRepository = statisticsRepository;
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

        return "private/dashboard_services/view_profile";
    }

    @GetMapping("/dashboard/user_list")
    public String userList(Model page) {
        List<com.palestra.palestra.pojo.User> allUsers = repo.getAllUserDetails();
        page.addAttribute("users", allUsers);

        return "private/dashboard_services/user_list";
    }

    @GetMapping("/dashboard/remove_expired_users")
    public String removeExpiredUsers(Model page, Authentication auth) {
        User authUser = ((User) Objects.requireNonNull(auth.getPrincipal()));
        int numberOfRemovedUsers = repo.removeExpiredUsers();
        page.addAttribute("number_of_removed_users", numberOfRemovedUsers);
        page.addAttribute("username", authUser.getUsername());
        return "private/dashboard_services/admin";
    }

    @GetMapping("/dashboard/upgrade")
    public String upgradeProfile(Model page, Authentication auth) {
        page.addAttribute("role", auth.getAuthorities().iterator().next().toString().replace("ROLE_", ""));
        return "private/dashboard_services/upgrade";
    }

    @GetMapping("/dashboard/training")
    public String defaultPrograms(Model page, Authentication auth) {
        List<Program> programs = programService.getDefaultPrograms();
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_PRO"))) {
            programs.addAll(customPrograms.getCustomPrograms(((User) Objects.requireNonNull(auth.getPrincipal())).getUsername()));
        }

        page.addAttribute("defaultPrograms", programs);
        return "private/dashboard_services/training";
    }

    @GetMapping("/dashboard/training_details")
    public String programDetails(Model page, Authentication auth, @RequestParam String programName) {
        List<Exercise> exercises = null;
        int calories = 0;
        try {
            exercises = programService.getProgramExercises(((User) Objects.requireNonNull(auth.getPrincipal())).getUsername(), programName);
            calories = programService.getProgramCalories(((User) auth.getPrincipal()).getUsername(), programName);
        } catch (ClassNotFoundException e) {
            page.addAttribute("notFound", true);
        }

        page.addAttribute("programName", programName);
        page.addAttribute("exercises", exercises);
        page.addAttribute("calories", calories);
        return "private/dashboard_services/training_details";
    }

    @GetMapping("/dashboard/complete_training")
    public String completeProgram(Model page, Authentication auth, @RequestParam String programName) {
        User authUser = ((User) Objects.requireNonNull(auth.getPrincipal()));

        try {
            programService.completeProgram(authUser, programName);
        } catch (IllegalAccessException e) {
            page.addAttribute("error", "Il tuo utente ha finito gli allenamenti, esegui l'upgrade del tuo account per eseguirne altri");
            return "private/error";
        }
        return "redirect:/dashboard";
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
        return "private/dashboard_services/upgrade";
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
        return "private/dashboard_services/change_password";
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
        return "private/dashboard_services/change_password";
    }

    @GetMapping("/dashboard/review")
    public String reviewForm() {
        return "private/dashboard_services/review";
    }

    @GetMapping("/dashboard/insert_program")
    public String insertProgramForm() {
        return "private/dashboard_services/insert_program";
    }

    @PostMapping("/dashboard/insert_program")
    public String insertProgramInDB(
            @RequestParam String programName,
            @RequestParam String exercises,
            Model page,
            Authentication auth
    ) {
        boolean success = true;
        try {
            int calories = customPrograms.addProgramToDB(((User) Objects.requireNonNull(auth.getPrincipal())).getUsername(), programName, exercises);
            page.addAttribute("calories", calories);
        } catch (JacksonException e) {
            success = false;
            page.addAttribute("reason", "L'input inviato non è valido!");
        } catch (IllegalStateException e) {
            success = false;
            page.addAttribute("reason", "Esiste già un programma con quel nome!");
        }
        page.addAttribute("success", success);
        return "private/dashboard_services/insert_program";
    }

    @GetMapping("/dashboard/personal_stats")
    public String personalStats(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        List<PersonalStatEntry> resultSet = statisticsRepository.getPersonalStats(username);
        page.addAttribute("username", username);
        page.addAttribute("labels", resultSet.stream().map(PersonalStatEntry::getProgram).toList());
        page.addAttribute("values", resultSet.stream().map(PersonalStatEntry::getTimes).toList());
        return "private/dashboard_services/personal_stats";
    }

    @GetMapping("/dashboard/global_stats")
    public String globalStats(Model page) {
        List<GlobalStatEntry> resultSet = statisticsRepository.getGlobalStatistics();
        page.addAttribute("global_stats", resultSet);
        return "private/dashboard_services/global_stats";
    }
}
