package org.nandwal.spring.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<Course> getAllCourses(String topicId) {
        List<Course> courses = new ArrayList<>();
        courseRepository.findByTopicId(topicId).forEach(courses::add);
        return courses.isEmpty() ? null : courses;
    }

    @Transactional(readOnly = true)
    public Course getCourse(String id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Transactional
    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    @Transactional
    public void updateCourse(Course course) {
        courseRepository.save(course);
    }


    @Transactional
    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }
}
