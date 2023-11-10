package com.book.gpt.dao;

import com.book.gpt.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BookDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public BookDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public BookDTO getBookInfo(int bookId) {
        String sql = "SELECT * FROM Book WHERE ID = ?";

        return jdbcTemplate.queryForObject(sql, new RowMapper<BookDTO>() {
            @Override
            public BookDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new BookDTO(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("AUTHOR"),
                        rs.getString("PUBLISHER"),
                        rs.getString("GENRE"),
                        rs.getString("IMAGE_URL"),
                        rs.getString("CONTENT_URL"),
                        rs.getString("SUMMARY"),
                        rs.getInt("PRICE"),
                        rs.getDate("PUBLISH_YEAR"),
                        rs.getDate("ENTRY_TIME"),
                        rs.getInt("PURCHASE_COUNT")
                );
            }
        }, bookId);
    }

    public Boolean checkPurchase(String memberId, int bookId) {
        String sql = "SELECT COUNT(*) FROM BUY WHERE MEMBER_ID = ? AND BOOK_ID = ?";

        int count = jdbcTemplate.queryForObject(sql, new Object[]{memberId, bookId}, Integer.class);

        return count > 0;
    }
    public Boolean purchaseBook(String memberId, int bookId, int price) {
        try {
            String checkCashSql = "SELECT cash FROM MEMBER WHERE ID = ?";
            int cash = jdbcTemplate.queryForObject(checkCashSql, new Object[]{memberId}, Integer.class);

            if (cash >= price) {
                String updateCashSql = "UPDATE MEMBER SET cash = cash - ? WHERE ID = ?";
                jdbcTemplate.update(updateCashSql, price, memberId);

                String insertPurchaseSql = "INSERT INTO BUY (MEMBER_ID, BOOK_ID) VALUES (?, ?)";
                jdbcTemplate.update(insertPurchaseSql, memberId, bookId);

                // 책의 PURCHASE_COUNT를 증가
                String updatePurchaseCountSql = "UPDATE BOOK SET PURCHASE_COUNT = PURCHASE_COUNT + 1 WHERE ID = ?";
                jdbcTemplate.update(updatePurchaseCountSql, bookId);

                System.out.println(cash);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public Boolean purchaseBooks(String memberId, List<Integer> bookIds) {
        try {
            String checkCashSql = "SELECT cash FROM MEMBER WHERE ID = ?";
            int cash = jdbcTemplate.queryForObject(checkCashSql, new Object[]{memberId}, Integer.class);

            int totalCost = 0;
            for (int bookId : bookIds) {
                String bookPriceSql = "SELECT price FROM Book WHERE ID = ?";
                int bookPrice = jdbcTemplate.queryForObject(bookPriceSql, new Object[]{bookId}, Integer.class);
                totalCost += bookPrice;
                String removeCartSql = "DELETE FROM CART WHERE MEMBER_ID = ? AND BOOK_ID = ?";
                jdbcTemplate.update(removeCartSql, memberId, bookId);
            }

            if (cash >= totalCost) {
                for (int bookId : bookIds) {
                    String bookPriceSql = "SELECT price FROM Book WHERE ID = ?";
                    int bookPrice = jdbcTemplate.queryForObject(bookPriceSql, new Object[]{bookId}, Integer.class);

                    String updateCashSql = "UPDATE MEMBER SET cash = cash - ? WHERE ID = ?";
                    jdbcTemplate.update(updateCashSql, bookPrice, memberId);

                    String insertPurchaseSql = "INSERT INTO BUY (MEMBER_ID, BOOK_ID) VALUES (?, ?)";
                    jdbcTemplate.update(insertPurchaseSql, memberId, bookId);

                    // 책의 PURCHASE_COUNT를 증가
                    String updatePurchaseCountSql = "UPDATE BOOK SET PURCHASE_COUNT = PURCHASE_COUNT + 1 WHERE ID = ?";
                    jdbcTemplate.update(updatePurchaseCountSql, bookId);
                }

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

}
