package org.nandwal.spring.course;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nandwal.spring.topic.Topic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourseServiceTests {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    public CourseServiceTests() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = List.of(new Course("1", "Course 1", "Description 1", "Topic 1"), new Course("2", "Course 2", "Description 2", "Topic 1"));
        when(courseRepository.findByTopicId("topic1")).thenReturn(courses);

        List<Course> result = courseService.getAllCourses("topic1");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllCourses1() {
        when(courseRepository.findByTopicId("topic1")).thenReturn(List.of());

        List<Course> result = courseService.getAllCourses("topic1");

        assertNull(result);
    }

    @Test
    void testGetCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic 1");
        when(courseRepository.findById("1")).thenReturn(Optional.of(course));

        Course result = courseService.getCourse("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void testGetCourse1() {
        when(courseRepository.findById("1")).thenReturn(Optional.empty());

        Course result = courseService.getCourse("1");

        assertNull(result);
    }

    @Test
    void testAddCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic 1");

        courseService.addCourse(course);

        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @Transactional
    void testAddCourse_transactional() {
        Course course = new Course("cpp-c1","Cpp Course 1", "Description 1", "cpp");
        Topic cppTopic = new Topic("cpp", "C++", "C++ Programming");
        course.setTopic(cppTopic); // Set the topic that causes the exception

        when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.addCourse(course);
        });
        assertEquals("Exception thrown from addCourse for topic cpp", exception.getMessage());

        // Verify that no course was saved due to rollback
        assertFalse(courseRepository.findById(course.getId()).isPresent());
    }

    @Test
    void testUpdateCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic 1");

        courseService.updateCourse(course);

        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testDeleteCourse() {
        courseService.deleteCourse("1");

        verify(courseRepository, times(1)).deleteById("1");
    }
}