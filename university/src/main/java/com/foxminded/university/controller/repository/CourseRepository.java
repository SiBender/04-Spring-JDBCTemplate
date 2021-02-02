package com.foxminded.university.controller.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Teacher;

@Repository
public class CourseRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void add(Course course) {        
        String query = "INSERT INTO courses (course_name, course_description, teacher_id)"
                    + "VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(query, course.getName(), course.getDescription(), course.getTeacher().getId());
            if (logger.isDebugEnabled()) {
                logger.debug("Inset new course ({}, {}, {})", course.getName(), course.getDescription(), course.getTeacher().getId());
            }
        } catch (DataAccessException ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while creating new course", ex);
            }
        }
    }
    
    public List<Course> getByTeacher(Teacher teacher) {
        if (logger.isDebugEnabled()) { 
            logger.debug("Query courses by teacher (id = {})", teacher.getId());
        }
        
        String query = "SELECT course_id, course_name, course_description FROM courses "
                     + "WHERE teacher_id = ?";
        return jdbcTemplate.query(query, new Object[] {teacher.getId()}, (resultSet, rowNum) -> {
            Course course = new Course();
            course.setId(resultSet.getInt("course_id"));
            course.setName(resultSet.getString("course_name"));
            course.setDescription(resultSet.getString("course_description"));
            course.setTeacher(teacher);
            return course;
        });
    }
    
    public Course getById(int id) {
        if (logger.isDebugEnabled()) {
            logger.debug("Get course by id ({})", id);
        }
        
        String query = "SELECT course_id, course_name, course_description, courses.teacher_id, first_name, last_name "
                     + "FROM courses "
                     + "JOIN teachers ON courses.teacher_id = teachers.teacher_id "
                     + "WHERE course_id = ?";        
        Course output = jdbcTemplate.queryForObject(query, new Object[] {id}, (resultSet, rowNum) -> {
            Course course = new Course();
            course.setId(resultSet.getInt("course_id"));
            course.setName(resultSet.getString("course_name"));
            course.setDescription(resultSet.getString("course_description"));
            Teacher teacher = new Teacher();
            teacher.setId(resultSet.getInt("teacher_id"));
            teacher.setFirstName(resultSet.getString("first_name"));
            teacher.setLastName(resultSet.getString("last_name"));
            course.setTeacher(teacher);
            return course;
        });
        List<Group> coursesGroups = getGroupsByCourseId(id);
        output.setGroups(coursesGroups);
        return output;
    }

    private List<Group> getGroupsByCourseId(int id) {
        String query = "SELECT * FROM groups "
                     + "WHERE group_id IN "
                     + "(SELECT group_id FROM groups_courses WHERE course_id = ?)";
        return jdbcTemplate.query(query,  new Object[] {id}, (resultSet, rowNum) -> {
            Group group = new Group();
            group.setId(id);
            group.setName(resultSet.getString("group_name"));            
            return group;
        });
    }
}
