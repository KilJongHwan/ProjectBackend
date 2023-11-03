package com.book.gpt.dao;

import com.book.gpt.common.Common;
import com.book.gpt.dto.MemberDTO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;

public class MemberDAO {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;

    // 비밀번호를 해싱하는 메서드
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean loginCheck(String id, String pwd) {
        try {
            conn = Common.getConnection();
            String sql = "SELECT * FROM MEMBER WHERE ID = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, id);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String sqlPwd = rs.getString("PASSWORD"); // 데이터베이스에서 해싱된 비밀번호를 가져옴
                if (hashPassword(pwd).equals(sqlPwd)) {
                    return true; // 해싱된 비밀번호와 입력한 비밀번호가 일치하면 로그인 성공
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(pStmt);
        Common.close(conn);
        return false;
    }

    // 중복값 체크 메서드
    public boolean signupCheck(String id, String email, String phone) {
        try {
            conn = Common.getConnection();
            String sql = "SELECT * FROM MEMBER WHERE ID = ? OR EMAIL = ? OR TEL = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, id);
            pStmt.setString(2, email);
            pStmt.setString(3, phone);
            rs = pStmt.executeQuery();

            if (rs.next()) {
                // 아이디, 이메일 또는 전화번호 중 하나라도 중복되는 경우
                Common.close(rs);
                Common.close(pStmt);
                Common.close(conn);
                return false;
            } else {
                // 중복되는 정보가 없는 경우
                Common.close(rs);
                Common.close(pStmt);
                Common.close(conn);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 회원가입 완료 메서드
    public boolean signup(MemberDTO member) {
        try {
            conn = Common.getConnection();
            String sql = "INSERT INTO MEMBER(ID, PASSWORD, NAME, EMAIL, TEL, CASH) VALUES(?, ?, ?, ?, ?, ?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, member.getId());
            pStmt.setString(2, hashPassword(member.getPassword())); // 해싱된 비밀번호를 저장
            pStmt.setString(3, member.getName());
            pStmt.setString(4, member.getEmail());
            pStmt.setString(5, member.getTel());
            pStmt.setInt(6, member.getCash());

            int rowsAffected = pStmt.executeUpdate();

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Common.close(pStmt);
            Common.close(conn);
        }
    }

}