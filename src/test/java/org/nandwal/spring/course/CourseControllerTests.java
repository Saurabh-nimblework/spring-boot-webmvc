package org.nandwal.spring.course;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
public class CourseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    void testGetAllCourses() throws Exception {
        List<Course> courses = List.of(new Course("1", "Course 1", "Description 1", "Topic1"), new Course("2", "Course 2", "Description 2", "Topic1"));
        when(courseService.getAllCourses("topic1")).thenReturn(courses);

        mockMvc.perform(get("/topics/topic1/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(model().attribute("courses", courses));
        verify(courseService, times(1)).getAllCourses("topic1");
    }

    @Test
    void testGetCourse() throws Exception {
        Course course = new Course("1", "Course 1", "Description 1", "Topic1");
        when(courseService.getCourse("1")).thenReturn(course);

        mockMvc.perform(get("/topics/topic1/courses/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(model().attribute("courses", course));
        verify(courseService, times(1)).getCourse("1");
    }

    @Test
    void testAddCourse() throws Exception {
        Course course = new Course("1", "Course 1", "Description 1", "Topic1");

        mockMvc.perform(post("/topics/Topic1/courses")
                        .contentType("application/json")
                        .content("{\"id\":\"1\",\"name\":\"Course 1\",\"description\":\"Description 1\"}"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics/Topic1/courses"));
        verify(courseService, times(1)).addCourse(any(Course.class));
    }

    @Test
    void testUpdateCourse() throws Exception {
        Course course = new Course("1", "Course 1", "Description 1", "Topic1");

        mockMvc.perform(put("/topics/Topic1/courses/1")
                        .contentType("application/json")
                        .content("{\"id\":\"1\",\"name\":\"Course 1\",\"description\":\"Description 1\"}"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics/Topic1/courses"));

        verify(courseService, times(1)).updateCourse(any(Course.class));
    }

    @Test
    void testDeleteCourse() throws Exception {
        mockMvc.perform(delete("/topics/Topic1/courses/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics/Topic1/courses"));

        verify(courseService, times(1)).deleteCourse("1");
    }
}