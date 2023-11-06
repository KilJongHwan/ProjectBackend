package com.book.gpt.controller;

import com.book.gpt.dao.ReviewDAO;
import com.book.gpt.dto.ReviewDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

// Spring Boot의 경우
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/purchase")
public class ReviewController {

    private ReviewDAO reviewDAO;

    @PostMapping("/review")
    public boolean addReview(@RequestBody ReviewDTO review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_SPECIAL")) {
                return reviewDAO.addReview(review);
            }
        }

        throw new RuntimeException("특별한 권한을 가진 사용자만 리뷰를 등록할 수 있습니다.");
    }

}