package org.nandwal.spring.course;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourseControllerTests {

    @Mock
    private CourseService courseService;

    @Mock
    private Model model;

    @InjectMocks
    private CourseController courseController;

    public CourseControllerTests() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = List.of(new Course("1", "Course 1", "Description 1", "Topic 1"), new Course("2", "Course 2", "Description 2", "Topic 1"));
        when(courseService.getAllCourses("topic1")).thenReturn(courses);

        String viewName = courseController.getAllCourses("topic1", model);

        assertEquals("courses", viewName);
        verify(model, times(1)).addAttribute("courses", courses);
        verify(courseService, times(1)).getAllCourses("topic1");
    }

    @Test
    void testGetAllCourses1() {
        when(courseService.getAllCourses("topic1")).thenReturn(null);

        String viewName = courseController.getAllCourses("topic1", model);

        assertEquals("courses", viewName);
        verify(model, times(1)).addAttribute("courseNotFound", true);
        verify(courseService, times(1)).getAllCourses("topic1");
    }

    @Test
    void testGetCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic 1");
        when(courseService.getCourse("1")).thenReturn(course);

        String viewName = courseController.getCourse("1", model);

        assertEquals("courses", viewName);
        verify(model, times(1)).addAttribute("courses", course);
        verify(courseService, times(1)).getCourse("1");
    }

    @Test
    void testGetCourse1() {
        when(courseService.getCourse("1")).thenReturn(null);

        String viewName = courseController.getCourse("1", model);

        assertEquals("courses", viewName);
        verify(model, times(1)).addAttribute("courseNotFound", true);
        verify(courseService, times(1)).getCourse("1");
    }

    @Test
    void testAddCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic 1");

        String viewName = courseController.addCourse(course, "topic1");

        assertEquals("redirect:/topics/topic1/courses", viewName);
        verify(courseService, times(1)).addCourse(course);
    }

    @Test
    void testUpdateCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic 1");

        String viewName = courseController.updateCourse(course, "1", "topic1");

        assertEquals("redirect:/topics/topic1/courses", viewName);
        verify(courseService, times(1)).updateCourse(course);
    }

    @Test
    void testDeleteCourse() {
        String viewName = courseController.deleteCourse("1", "topic1");

        assertEquals("redirect:/topics/topic1/courses", viewName);
        verify(courseService, times(1)).deleteCourse("1");
    }
}