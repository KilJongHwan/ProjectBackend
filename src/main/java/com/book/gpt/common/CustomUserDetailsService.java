package com.book.gpt.common;

import com.book.gpt.dao.MemberDAO;
import com.book.gpt.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private MemberDAO userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        MemberDTO dto = userRepository.findId(id);
        if (dto == null) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
        return new CustomUserDetails(dto.getId(), dto.getPassword(),dto.getAuthorities());
    }
}
