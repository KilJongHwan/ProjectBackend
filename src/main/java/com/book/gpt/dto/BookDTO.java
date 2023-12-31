package com.book.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class BookDTO {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String genre;
    private String imageUrl;
    private String contentUrl;
    private String summary;
    private int price;
    private Date publishYear;
    private Date entry;
    private int purchaseCount;
}
