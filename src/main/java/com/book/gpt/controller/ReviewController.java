package com.book.gpt.controller;

import com.book.gpt.dao.ReviewDAO;
import com.book.gpt.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Spring Boot의 경우
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/purchase")
public class ReviewController {

    @Autowired
    private ReviewDAO reviewDAO;

    @PostMapping("/review")
    public ResponseEntity<ReviewDTO> addReview(@RequestBody ReviewDTO review) {
        ReviewDTO createdReview = reviewDAO.addReview(review);
        System.out.println(createdReview);
        if (createdReview != null) {
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/reviewdata/{bookid}")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable  int bookid) {
        List<ReviewDTO> reviews = reviewDAO.getReviews(bookid);
        if (reviews != null && !reviews.isEmpty()) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/reviewdata/stats/{bookid}")
    public ResponseEntity<ReviewDTO> getReviewStats(@PathVariable int bookid) {
        ReviewDTO stats = reviewDAO.getReviewStats(bookid);
        if (stats.getAverageRating() != 0 || stats.getTotalReviews() != 0) {
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}