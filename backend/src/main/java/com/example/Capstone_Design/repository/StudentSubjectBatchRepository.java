package com.example.Capstone_Design.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentSubjectBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveSubjects(String studentNumber, List<String> subjectNames) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO student_subject (student_number, subject_name) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, studentNumber);
                        ps.setString(2, subjectNames.get(i));
                    }

                    public int getBatchSize() {
                        return subjectNames.size();
                    }
                }
        );
    }

}
