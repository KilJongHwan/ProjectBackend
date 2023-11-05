package com.book.gpt.controller;

import com.book.gpt.JWT.JwtAuthorizationFilter;
import com.book.gpt.dao.MemberDAO;
import com.book.gpt.dto.MemberDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class MemberController {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> memberLogin(@RequestBody Map<String, String> loginData) {
        String id = loginData.get("id");
        String pwd = loginData.get("password");

        MemberDAO dao = new MemberDAO();

        boolean loginResult = dao.loginCheck(id, pwd);
        System.out.println(loginResult);
        if (loginResult) {
            // 로그인 성공 시 토큰 생성
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

            String token = Jwts.builder()
                    .setSubject(id)
                    .signWith(key)
                    .compact();
            // 클라이언트에게 토큰 반환
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("", HttpStatus.OK);
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
    @GetMapping("/check-login")
    public ResponseEntity<String> checkLogin(HttpServletRequest request) {
        try {
            // JWTAuthorizationFilter에서 사용한 방식과 동일한 방식으로 토큰을 추출
            String token = jwtAuthorizationFilter.extractTokenFromRequest(request);

            if (token != null) {
                // 토큰이 유효하다면 로그인 상태
                System.out.println("로그인이 된 상태입니다..");
                return new ResponseEntity<>("User is logged in", HttpStatus.OK);
            } else {
                // 토큰이 없거나 유효하지 않다면 비로그인 상태
                System.out.println("로그인이 필요 합니다");
                return new ResponseEntity<>("User is not logged in", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
