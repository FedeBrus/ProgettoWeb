package com.palestra.palestra.Controller;

import com.palestra.palestra.Repositories.StatisticsRepository;
import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.pojo.Stats.GlobalStatEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;

@Controller
public class DashboardAdminController {
    private final UserRepository repo;
    private final StatisticsRepository statisticsRepository;

    @Autowired
    public DashboardAdminController(
            UserRepository repo,
            StatisticsRepository statisticsRepository
    ) {
        this.repo = repo;
        this.statisticsRepository = statisticsRepository;
    }

    @GetMapping("/dashboard/admin/user_list")
    public String userList(Model page) {
        List<com.palestra.palestra.pojo.Users.User> allUsers = repo.getAllUserDetails();
        page.addAttribute("users", allUsers);

        return "private/services/admin_services/user_list";
    }

    @GetMapping("/dashboard/admin/remove_expired_users")
    public String removeExpiredUsers(Model page, Authentication auth) {
        User authUser = ((User) Objects.requireNonNull(auth.getPrincipal()));
        int numberOfRemovedUsers = repo.removeExpiredUsers();
        page.addAttribute("number_of_removed_users", numberOfRemovedUsers);
        page.addAttribute("username", authUser.getUsername());
        return "private/dashboard/admin";
    }

    @GetMapping("/dashboard/admin/global_stats")
    public String globalStats(Model page) {
        List<GlobalStatEntry> resultSet = statisticsRepository.getGlobalStatistics();
        page.addAttribute("global_stats", resultSet);
        return "private/services/admin_services/global_stats";
    }
}
