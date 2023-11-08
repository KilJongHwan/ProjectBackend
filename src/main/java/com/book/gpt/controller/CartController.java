package com.book.gpt.controller;

import com.book.gpt.dao.CartDAO;
import com.book.gpt.dto.CartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartDAO cartDAO;

    // 장바구니 아이템 추가
    @PostMapping("/add")
    public ResponseEntity<Void> addCart(@RequestBody CartDTO cart) {
        cartDAO.addCart(cart);
        return ResponseEntity.ok().build();
    }

    // 장바구니 아이템 가져오기
    @GetMapping("/{memberId}")
    public ResponseEntity<List<CartDTO>> getCartItems(@PathVariable String memberId) {
        List<CartDTO> cartItems = cartDAO.getCartItems(memberId);
        return ResponseEntity.ok(cartItems);
    }

    // 장바구니 아이템 제거
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable int cartId) {
        cartDAO.removeFromCart(cartId);
        return ResponseEntity.ok().build();
    }
}

