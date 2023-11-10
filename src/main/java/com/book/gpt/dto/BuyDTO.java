package com.book.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
@AllArgsConstructor
public class BuyDTO {
    private int userId;
    private int bookId;
    private Date purchaseDate;
}
