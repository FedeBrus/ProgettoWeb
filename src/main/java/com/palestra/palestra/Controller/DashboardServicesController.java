package com.palestra.palestra.Controller;

import com.palestra.palestra.Repositories.StatisticsRepository;
import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.Services.Programs.CustomProgramService;
import com.palestra.palestra.Services.Programs.ProgramService;
import com.palestra.palestra.Services.UserActions.UserUtils;
import com.palestra.palestra.pojo.Programs.Exercise;
import com.palestra.palestra.pojo.Stats.PersonalStatEntry;
import com.palestra.palestra.pojo.Programs.Program;
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

    @GetMapping("/dashboard/user/profile")
    public String userProfile(Model page, Authentication auth) {
        User authUser = ((User) Objects.requireNonNull(auth.getPrincipal()));
        com.palestra.palestra.pojo.Users.User u = repo.getUserDetails(authUser.getUsername());
        page.addAttribute("username", authUser.getUsername());
        page.addAttribute("name", u.getName());
        page.addAttribute("surname", u.getSurname());
        page.addAttribute("dob", u.getDate_of_birth());
        page.addAttribute("role", authUser.getAuthorities().iterator().next().toString());

        return "private/services/users_services/view_profile";
    }

    @GetMapping("/dashboard/user/upgrade")
    public String upgradeProfile(Model page, Authentication auth) {
        page.addAttribute("role", auth.getAuthorities().iterator().next().toString().replace("ROLE_", ""));
        return "private/services/users_services/upgrade";
    }

    @GetMapping("/dashboard/user/training")
    public String defaultPrograms(Model page, Authentication auth) {
        List<Program> programs = programService.getDefaultPrograms();
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_PRO"))) {
            programs.addAll(customPrograms.getCustomPrograms(((User) Objects.requireNonNull(auth.getPrincipal())).getUsername()));
        }

        page.addAttribute("defaultPrograms", programs);
        return "private/services/users_services/training";
    }

    @GetMapping("/dashboard/user/training_details")
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
        return "private/services/users_services/training_details";
    }

    @GetMapping("/dashboard/user/complete_training")
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
        return "private/services/users_services/upgrade";
    }

    @PostMapping("/dashboard/user/upgrade/basic")
    public String upgradeProfileBasic(Model page, Authentication auth) {
        return updateProfile(page, auth, "ROLE_USER_BASIC");
    }

    @PostMapping("/dashboard/user/upgrade/pro")
    public String upgradeProfilePro(Model page, Authentication auth) {
        return updateProfile(page, auth, "ROLE_USER_PRO");
    }

    @GetMapping("/dashboard/user/change_password")
    public String changePassword(Model page, Authentication auth) {
        return "private/services/users_services/change_password";
    }

    @PostMapping("/dashboard/user/change_password")
    public String changePasswordPost(Model page,
                                     Authentication auth,
                                     @RequestParam String password) {
        User u = (User) auth.getPrincipal();
        if(utils.changePassword(Objects.requireNonNull(u).getUsername(), password)) {
            page.addAttribute("success", true);
        } else {
            page.addAttribute("success", false);
        }
        return "private/services/users_services/change_password";
    }

    @GetMapping("/dashboard/user/review")
    public String reviewForm() {
        return "private/services/users_services/review";
    }

    @GetMapping("/dashboard/user/insert_program")
    public String insertProgramForm() {
        return "private/services/users_services/insert_program";
    }

    @PostMapping("/dashboard/user/insert_program")
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
        return "private/services/users_services/insert_program";
    }

    @GetMapping("/dashboard/user/personal_stats")
    public String personalStats(Model page, Authentication auth) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        List<PersonalStatEntry> resultSet = statisticsRepository.getPersonalStats(username);
        page.addAttribute("username", username);
        page.addAttribute("labels", resultSet.stream().map(PersonalStatEntry::getProgram).toList());
        page.addAttribute("values", resultSet.stream().map(PersonalStatEntry::getTimes).toList());
        return "private/services/users_services/personal_stats";
    }
}
