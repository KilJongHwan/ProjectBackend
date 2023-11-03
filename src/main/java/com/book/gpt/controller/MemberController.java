package com.book.gpt.controller;

import com.book.gpt.dao.MemberDAO;
import com.book.gpt.dto.MemberDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class MemberController {
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Boolean> memberLogin(@RequestBody Map<String, String> loginData) {
        String id = loginData.get("id");
        String pwd = loginData.get("password");

        MemberDAO dao = new MemberDAO();


        boolean loginResult  = dao.loginCheck(id, pwd);

        System.out.println("ID : " + id);
        System.out.println("PWD : " + pwd);

        if (loginResult) {
            // 사용자의 정보를 포함한 응답 생성
            return new ResponseEntity<>(loginResult, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Boolean> memberSignup(@RequestBody MemberDTO memberDTO) {
        MemberDAO dao = new MemberDAO();
        boolean regResult = false;

        // 비밀번호를 해싱해서 저장
        String plainPassword = memberDTO.getPassword();
        memberDTO.setPassword(plainPassword);

        if (dao.signupCheck(memberDTO.getId(), memberDTO.getEmail(), memberDTO.getTel())) {
            // 회원 가입을 수행
            regResult = dao.signup(memberDTO);
            System.out.println("회원가입");
        } else {
            System.out.println("중복된 아이디, 이메일, 전화 번호가 존재합니다 ");
        }

        return new ResponseEntity<>(regResult, HttpStatus.OK);

    }

}
