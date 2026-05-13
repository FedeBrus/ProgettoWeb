package com.palestra.palestra.Controller;

import com.palestra.palestra.Repositories.ReviewRepository;
import com.palestra.palestra.Repositories.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Controller
public class AJAXController {
    private final ReviewRepository repo;

    @Autowired
    public AJAXController(ReviewRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/dashboard/review")
    @ResponseBody
    public String insertReview(Authentication auth,  @RequestParam @NonNull String review) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        repo.insertReview(username, review);
        return "";
    }
}
