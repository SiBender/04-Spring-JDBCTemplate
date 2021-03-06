package com.foxminded.university.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.university.controller.repository.GroupRepository;
import com.foxminded.university.controller.repository.StudentRepository;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@Service
public class StudentsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    
    @Autowired
    public StudentsService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public Student getStudent(int studentId) {
        if (logger.isInfoEnabled()) {
            logger.info("Get student by id ({})", studentId);
        }
        return studentRepository.getById(studentId);
    }
    
    public Group getGroupByStudent(int studentId) {
        if (logger.isInfoEnabled()) {
            logger.info("Get group by student id ({})", studentId);
        }
        Student student = new Student();
        student.setId(studentId);
        return groupRepository.getByStudent(student);
    }
    
    public Group getGroupById(int groupId) {
        if (logger.isInfoEnabled()) {logger.info("Get group by id ({})", groupId);}
        return groupRepository.getById(groupId);
    }
}
