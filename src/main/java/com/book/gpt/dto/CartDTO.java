package com.book.gpt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {
    private int cartId;
    private int bookId;
    private String memberId;
}
