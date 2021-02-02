package com.foxminded.university.controller.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.university.model.Lesson;

@Repository
public class LessonRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LessonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void add(Lesson lesson) {
        String query = "INSERT INTO lessons (lesson_date, timeslot_id, course_id, classroom_id) "
                     + "VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(query, lesson.getDate(), lesson.getTime().getId(),
                                lesson.getCourse().getId(), lesson.getClassroom().getId());
            if (logger.isDebugEnabled()) {
                logger.debug("Insert new lesson({}, {}, {}, {})", lesson.getDate(), lesson.getTime().getId(),
                            lesson.getCourse().getId(), lesson.getClassroom().getId());
            }
        } catch (DataAccessException ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while creating new lesson", ex);
            }
            throw ex;
        }                    
    }  
}
