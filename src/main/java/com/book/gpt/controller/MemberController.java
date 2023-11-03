package com.book.gpt.controller;

import com.book.gpt.dao.MemberDAO;
import com.book.gpt.dto.MemberDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        MemberDAO dao = new MemberDAO();
        boolean regResult = false;

        // 비밀번호를 해싱해서 저장
        String plainPassword = memberDTO.getPassword();
        String hashedPassword = dao.hashPassword(plainPassword);
        memberDTO.setPassword(hashedPassword);

        if (dao.signupCheck(memberDTO.getId(), memberDTO.getEmail(), memberDTO.getTel())) {
            // 회원 가입을 수행
            regResult = dao.signup(memberDTO);
        }

        return new ResponseEntity<>(regResult, HttpStatus.OK);
    }

}
