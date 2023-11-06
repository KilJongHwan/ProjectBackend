package com.book.gpt.dao;

import com.book.gpt.common.Common;
import com.book.gpt.dto.ReviewDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReviewDAO {
    private Connection conn = null;
    private PreparedStatement pStmt = null;

    public boolean addReview(ReviewDTO review) {
        try {
            conn = Common.getConnection();
            String sql = "INSERT INTO REVIEW (REVIEW_ID, MEMBER_ID, BOOK_ID, RATING, CONTENT, CREATION_DATE) VALUES (?, ?, ?, ?, ?, ?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, review.getReviewId());
            pStmt.setString(2, review.getMemberId());
            pStmt.setInt(3, review.getBookId());
            pStmt.setDouble(4, review.getRating());
            pStmt.setString(5, review.getContent());
            pStmt.setDate(6, review.getCreationDate());

            int rowsAffected = pStmt.executeUpdate();

            if (rowsAffected > 0) {
                Common.close(pStmt);
                Common.close(conn);
                return true;
            } else {
                // 삽입 실패
                Common.close(pStmt);
                Common.close(conn);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
