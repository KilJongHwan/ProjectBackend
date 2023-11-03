package com.book.gpt.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class BookDTO {
    private int bookNo;
    private String title;
    private String author;
    private String publisher;
    private String genre;
    private String imageUrl;
    private String contentUrl;
    private String summary;
    private double price;
    private Date publishYear;
    private Date entry;
    private int purchaseCount;
}
