package com.book.gpt.dao;

import com.book.gpt.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
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

    public boolean loginCheck(String id, String hashedPwd) {
        String sql = "SELECT PASSWORD FROM MEMBER WHERE ID = ?";
        List<String> results = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> rs.getString("PASSWORD"));

        if (results.isEmpty()) {
            return false;
        }

        String sqlPwd = results.get(0);

        return hashedPwd.equals(sqlPwd);
    }


    public boolean signupCheck(String id, String email, String phone) {
        String sql = "SELECT * FROM MEMBER WHERE ID = ? OR EMAIL = ? OR TEL = ?";
        List<MemberDTO> results = jdbcTemplate.query(sql, new Object[]{id, email, phone}, (rs, rowNum) -> {
            MemberDTO member = new MemberDTO();
            member.setId(rs.getString("ID"));
            member.setEmail(rs.getString("EMAIL"));
            member.setTel(rs.getString("TEL"));
            return member;
        });
        return results.isEmpty();
    }

    public boolean signup(MemberDTO member) {
        String sql = "INSERT INTO MEMBER(ID, PASSWORD, NAME, EMAIL, TEL, CASH, PROFILE_URL) VALUES(?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, member.getId(), member.getPassword(), member.getName(), member.getEmail(), member.getTel(), member.getCash(), member.getProfileUrl()) > 0;
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