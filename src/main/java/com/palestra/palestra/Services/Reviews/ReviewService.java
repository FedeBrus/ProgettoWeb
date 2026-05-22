package com.palestra.palestra.Services.Reviews;

import com.palestra.palestra.Repositories.ReviewRepository;
import com.palestra.palestra.pojo.Reviews.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository repo;

    @Autowired
    public ReviewService(ReviewRepository repo) {
        this.repo = repo;
    }

    public void insertReview(String username, String review) {
        repo.insertReview(username, review);
    }

    public List<Review> getReviews() {
        return repo.getReviews();
    }
}
