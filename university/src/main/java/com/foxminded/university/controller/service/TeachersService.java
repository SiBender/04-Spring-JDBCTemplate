package com.foxminded.university.controller.service;

import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.university.controller.repository.CourseRepository;
import com.foxminded.university.controller.repository.TeacherRepository;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

@Service
public class TeachersService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    
    @Autowired
    public TeachersService(TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }
    
    public Teacher getTeacher(int teacherId) {
        if (logger.isInfoEnabled()) {logger.info("Get teacher by id ({})", teacherId);}
        return teacherRepository.getById(teacherId);
    }
    
    public void createCourse(String name, String description, int teacherId) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("Try to create new course ({}, {}, {})", name, description, teacherId);
            }
            Teacher teacher = new Teacher();
            teacher.setId(teacherId);
            
            Course course = new Course();
            course.setName(name);
            course.setDescription(description);
    
            courseRepository.add(course);
            if (logger.isInfoEnabled()) { logger.info("Course created successfully"); }
        } catch (DateTimeParseException ex) {
            if (logger.isErrorEnabled()) { 
                logger.error("Error while creating course", ex);
            }
        }
    }
    
    public List<Course> getCoursesByTeacher(int teacherId) {
        if (logger.isInfoEnabled()) { logger.info("Get courses by teacher id ({})", teacherId); }
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        return courseRepository.getByTeacher(teacher);
    }
    
    public Course getCourse(int courseId) {
        if (logger.isInfoEnabled()) { logger.info("Get courses by course id ({})", courseId); }
        return courseRepository.getById(courseId);
    }
}
