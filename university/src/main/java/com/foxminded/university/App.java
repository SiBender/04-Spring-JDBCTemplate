package com.foxminded.university;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.foxminded.university.controller.repository.CourseRepository;
import com.foxminded.university.controller.repository.StudentRepository;
import com.foxminded.university.controller.service.TimetableService;
import com.foxminded.university.controller.util.DateIntervalGenerator;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) {
        logger.info("App has started");
        try {
            ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");    
            logger.info("Context built");
            
            StudentRepository stRep = context.getBean(StudentRepository.class);
            System.out.println(stRep.getCount());            
    
            DateIntervalGenerator intervalGenerator = context.getBean(DateIntervalGenerator.class);
            intervalGenerator.getCurrentMonth();
         
            TimetableService ttServ = context.getBean(TimetableService.class);
            System.out.println(ttServ.getTeacherTimetable("2020-01-01", "2020-06-30", 1));
            System.out.println(ttServ.getStudentTimetable("2020-01-01", "2020-06-30", 1));
            
            logger.info("Shutting down application");
            context.close();
            logger.info("Context closed");
        } catch (BeansException ex) {
            logger.error("Error while building context", ex);
        } catch (Exception ex) {
            logger.error("Unexpected error ", ex);
        }
    }
}
