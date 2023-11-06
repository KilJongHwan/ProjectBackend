package com.book.gpt.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class ReviewDTO {
    private int reviewId;
    private String memberId;
    private int bookId;
    private double rating;
    private String content;
    private Date creationDate;
}