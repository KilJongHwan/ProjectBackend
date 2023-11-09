package com.book.gpt.dao;

import com.book.gpt.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
