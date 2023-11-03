package com.book.gpt.controller;

import com.book.gpt.dao.MemberDAO;
import com.book.gpt.dto.MemberDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class MemberController {
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Boolean> memberLogin(@RequestBody Map<String, String> loginData) {
        String id = loginData.get("id");
        String pwd = loginData.get("pwd");

        System.out.println("ID : " + id);
        System.out.println("PWD : " + pwd);
        MemberDAO dao = new MemberDAO();
        boolean rst = dao.loginCheck(id, pwd);

        return  new ResponseEntity<>(rst, HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<Boolean> memberSignup(@RequestBody MemberDTO memberDTO) {
        String id = memberDTO.getId();
        String pwd = memberDTO.getPassword();
        String email = memberDTO.getEmail();
        String phone = memberDTO.getTel();

        MemberDAO dao = new MemberDAO();
        boolean signupResult = dao.signupCheck(id,pwd, email, phone);

        if (signupResult) {

        }

        return new ResponseEntity<>(signupResult, HttpStatus.OK);
    }
}
