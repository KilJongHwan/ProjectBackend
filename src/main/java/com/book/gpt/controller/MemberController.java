package com.book.gpt.controller;

import com.book.gpt.dao.MemberDAO;
import com.book.gpt.dto.MemberDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

//    @PostMapping("/login")
//    public ResponseEntity<Boolean> memberLogin(@RequestBody Map<String, String> loginData) {
//        String id = loginData.get("id");
//        String pwd = loginData.get("password");
//
//        MemberDAO dao = new MemberDAO();
//
//
//        boolean loginResult  = dao.loginCheck(id, pwd);
//
//        System.out.println("ID : " + id);
//        System.out.println("PWD : " + pwd);
//
//        if (loginResult) {
//            // 사용자의 정보를 포함한 응답 생성
//            return new ResponseEntity<>(loginResult, HttpStatus.OK);
//
//        } else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//    }
    @PostMapping("/login")
    public ResponseEntity<String> memberLogin(@RequestBody Map<String, String> loginData) {
        String id = loginData.get("id");
        String pwd = loginData.get("password");

        MemberDAO dao = new MemberDAO();

        boolean loginResult = dao.loginCheck(id, pwd);

        if (loginResult) {
            // 로그인 성공 시 토큰 생성
            String token = Jwts.builder()
                    .setSubject(id) // 사용자 아이디를 토큰의 주체로 설정
                    .signWith(SignatureAlgorithm.HS256, "secretKey") // 서명 알고리즘 및 비밀 키
                    .compact();

            // 클라이언트에게 토큰 반환
            return new ResponseEntity<>(token, HttpStatus.OK);
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
