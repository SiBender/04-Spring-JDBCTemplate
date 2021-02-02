package com.foxminded.university.controller.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university.model.Classroom;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Timeslot;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/spring.xml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LessonRepositoryTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    LessonRepository lessonRepository;
    
    @Order(1)
    @Test
    void addShouldCreateNewRowInLessonsTable() {      
        LocalDate date = LocalDate.now();
        Timeslot time = new Timeslot();
        time.setId(1);
        Course course = new Course();
        course.setId(1);
        Classroom classroom = new Classroom();
        classroom.setId(1);
        
        Lesson lesson = new Lesson();
        lesson.setDate(date);
        lesson.setTime(time);
        lesson.setCourse(course);
        lesson.setClassroom(classroom);
        lessonRepository.add(lesson);
        
        int actual = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM lessons", Integer.class);
        assertEquals(3, actual);
    }
    
    @Order(2)
    @ParameterizedTest
    @CsvSource({"100, 1, 1",
                "1, 100, 1",
                "1, 1, 100"})
    void addShouldThrouwExceptionForIncorrectDataTest(int timeslotId, int courseId, int classroomId) {
        LocalDate date = LocalDate.now();
        Timeslot time = new Timeslot();
        time.setId(timeslotId);
        Course course = new Course();
        course.setId(courseId);
        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        
        Lesson lesson = new Lesson();
        lesson.setDate(date);
        lesson.setTime(time);
        lesson.setCourse(course);
        lesson.setClassroom(classroom);
        
        Throwable thrown = assertThrows(DataIntegrityViolationException.class, () -> {
            lessonRepository.add(lesson);
        });        
    }
}
