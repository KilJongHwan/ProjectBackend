package com.book.gpt.dao;

import com.book.gpt.common.Common;
import com.book.gpt.dto.ReviewDTO;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReviewDAO {
    private Connection conn = null;
    private PreparedStatement pStmt = null;
    private ResultSet rs = null;

    public ReviewDTO addReview(ReviewDTO review) {
        try {
            conn = Common.getConnection();
            String sql = "INSERT INTO REVIEW (MEMBER_ID, BOOK_ID, RATING, CONTENT, CREATION_DATE) VALUES (?, ?, ?, ?, ?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, review.getMemberId());
            pStmt.setInt(2, review.getBookId());
            pStmt.setDouble(3, review.getRating());
            pStmt.setString(4, review.getContent());
            pStmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis())); // 현재 시간을 Timestamp로 변환하여 삽입

            int rowsAffected = pStmt.executeUpdate();
            if (rowsAffected > 0) {
                return review;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        }
        return null;
    }

    public List<ReviewDTO> getReviews(int bookId) {
        List<ReviewDTO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            String sql = "SELECT * FROM REVIEW WHERE BOOK_ID = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, bookId);
            rs = pStmt.executeQuery();

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setMemberId(rs.getString("MEMBER_ID"));
                review.setBookId(rs.getInt("BOOK_ID"));
                review.setRating(rs.getDouble("RATING"));
                review.setContent(rs.getString("CONTENT"));
                review.setCreationDate(rs.getDate("CREATION_DATE"));
                list.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        }
        return list;
    }
}
