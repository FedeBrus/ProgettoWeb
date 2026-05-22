package com.palestra.palestra.Controller;

import com.palestra.palestra.Services.Reviews.ReviewService;
import com.palestra.palestra.pojo.Reviews.Review;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;

@Controller
public class AJAXController {
    private final ReviewService rs;

    @Autowired
    public AJAXController(ReviewService rs) {
        this.rs = rs;
    }

    @PostMapping("/dashboard/review")
    @ResponseBody
    public String insertReview(Authentication auth,  @RequestParam @NonNull String review) {
        String username = ((User) Objects.requireNonNull(auth.getPrincipal())).getUsername();
        rs.insertReview(username, review);
        return "";
    }

    @GetMapping("/reviews")
    @ResponseBody
    public List<Review> getReviews() {
        return rs.getReviews();
    }
}
