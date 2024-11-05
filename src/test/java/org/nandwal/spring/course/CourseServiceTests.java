package org.nandwal.spring.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nandwal.spring.topic.Topic;
import org.nandwal.spring.topic.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class CourseServiceTests {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TopicRepository topicRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        topicRepository.deleteAll();
        Topic topic = new Topic("Topic1", "Topic 1", "Description 1");
        topicRepository.save(topic);
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = List.of(new Course("1", "Course 1", "Description 1", "Topic1"), new Course("2", "Course 2", "Description 2", "Topic1"));
        courseRepository.saveAll(courses);

        List<Course> result = courseService.getAllCourses("Topic1");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic1");
        courseRepository.save(course);

        Course result = courseService.getCourse("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void testAddCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic1");
        courseService.addCourse(course);

        Optional<Course> result = courseRepository.findById("1");
        assertTrue(result.isPresent());
    }

    @Test
    void testAddCourse_Transactional() {
        Topic cppTopic = new Topic("cpp", "C++", "C++ Programming");
        topicRepository.save(cppTopic);
        Course course = new Course("cpp-c1", "Cpp Course 1", "Description 1", "cpp");
        course.setTopic(cppTopic);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.addCourse(course);
        });
        assertEquals("Exception thrown for topic cpp", exception.getMessage());

        // Verify that no course was saved due to rollback
        assertFalse(courseRepository.findById(course.getId()).isPresent());
    }

    @Test
    void testUpdateCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic1");
        courseRepository.save(course);
        course.setName("Updated Course 1");
        courseService.updateCourse(course);

        Optional<Course> result = courseRepository.findById("1");
        assertTrue(result.isPresent());
        assertEquals("Updated Course 1", result.get().getName());
    }

    @Test
    void testDeleteCourse() {
        Course course = new Course("1", "Course 1", "Description 1", "Topic1");
        courseRepository.save(course);
        courseService.deleteCourse("1");

        Optional<Course> result = courseRepository.findById("1");
        assertFalse(result.isPresent());
    }
}