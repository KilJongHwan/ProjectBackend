package com.book.gpt.JWT;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 실제 사용자 데이터베이스 또는 저장소에서 사용자 정보를 조회하여 UserDetails를 반환하는 코드를 작성해야 합니다.
        // 이 예제에서는 임의로 UserDetails를 생성하여 반환합니다.
        if ("user123".equals(username)) {
            return new CustomUserDetails("user123", "$2a$10$2Hkl3xJzqo1A4S59kwjdEO2ULdcC3Vwd/CAz1q4zfuAY3L9gF88Jq", "ROLE_USER");
        } else {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
    }
}
