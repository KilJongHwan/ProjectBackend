package com.book.gpt.dao;

import com.book.gpt.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;


@Repository

public class MemberDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public MemberDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hashedBytes.length);
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean loginCheck(String id, String pwd) {
        System.out.println(hashPassword(pwd));
        String sql = "SELECT * FROM MEMBER WHERE ID = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            String sqlPwd = rs.getString("PASSWORD");
            String hashedPwd = hashPassword(pwd);
            System.out.println(hashedPwd);
            return hashPassword(sqlPwd).equals(hashedPwd);
        }));
    }

    public boolean signupCheck(String id, String email, String phone) {
        String sql = "SELECT * FROM MEMBER WHERE ID = ? OR EMAIL = ? OR TEL = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, new Object[]{id, email, phone}, (rs, rowNum) -> !rs.next()));
    }

    public boolean signup(MemberDTO member) {
        String sql = "INSERT INTO MEMBER(ID, PASSWORD, NAME, EMAIL, TEL, CASH) VALUES(?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, member.getId(), hashPassword(member.getPassword()), member.getName(), member.getEmail(), member.getTel(), member.getCash()) > 0;
    }

    public MemberDTO findId(String id) {
        String sql = "SELECT * FROM MEMBER WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, memberRowMapper());
    }

    private RowMapper<MemberDTO> memberRowMapper() {
        return (rs, rowNum) -> {
            MemberDTO member = new MemberDTO();
            member.setId(rs.getString("ID"));
            member.setPassword(rs.getString("PASSWORD"));
            member.setName(rs.getString("NAME"));
            member.setEmail(rs.getString("EMAIL"));
            member.setTel(rs.getString("TEL"));
            member.setCash(rs.getInt("CASH"));
            String role = findRoleById(rs.getString("ID"));
            member.setRole(role);
            return member;
        };
    }

    public String findRoleById(String id) {
        String sql = "SELECT AUTH FROM MEMBER WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            String auth = rs.getString("AUTH");
            System.out.println(auth);
            return Objects.equals(auth, "0") ? "ROLE_USER" : "ROLE_ADMIN";
        });
    }

}