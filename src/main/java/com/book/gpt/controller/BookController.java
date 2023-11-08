package com.book.gpt.controller;

import com.book.gpt.dao.BookDAO;
import com.book.gpt.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookDAO bookDAO;

    // 책의 상세 정보 가져오기
    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookInfo(@PathVariable int bookId) {
        BookDTO bookInfo = bookDAO.getBookInfo(bookId);
        return ResponseEntity.ok(bookInfo);
    }
}