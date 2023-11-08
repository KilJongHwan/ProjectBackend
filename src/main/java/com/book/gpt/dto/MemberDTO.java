package com.book.gpt.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class MemberDTO {
    private String id;
    private String password;
    private String name;
    private String email;
    private String tel;
    private int cash;
    private int auth;
    private String profileUrl = null; // 프로필 이미지 URL
    private Collection<? extends GrantedAuthority> authorities; // 권한 정보
    private String role;

}
