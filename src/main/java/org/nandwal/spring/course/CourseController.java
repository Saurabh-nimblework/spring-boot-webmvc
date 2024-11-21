package org.nandwal.spring.course;

import org.nandwal.spring.topic.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping("/topics/{topicId}/courses")
    public String getAllCourses(@PathVariable String topicId, Model model) {
        List<Course> courses = courseService.getAllCourses(topicId);
        if (courses == null) {
            model.addAttribute("courseNotFound", true);
        } else {
            model.addAttribute("courses", courses);
        }
        return "courses";
    }

    @RequestMapping("/topics/{topicId}/courses/{id}")
    public String getCourse(@PathVariable String id, Model model) {
        Course course = courseService.getCourse(id);
        if (course == null) {
            model.addAttribute("courseNotFound", true);
        } else {
            model.addAttribute("courses", course);
        }
        return "courses";
    }

    @RequestMapping(method= RequestMethod.POST, value="/topics/{topicId}/courses")
    public String addCourse(@RequestBody Course course, @PathVariable String topicId) {
        course.setTopic(new Topic(topicId, "", ""));
        courseService.addCourse(course);
        return "redirect:/topics/" + topicId + "/courses"; // Redirect to the list of courses
    }

    @RequestMapping(method= RequestMethod.PUT, value="/topics/{topicId}/courses/{id}")
    public String updateCourse(@RequestBody Course course, @PathVariable String id, @PathVariable String topicId) {
        course.setTopic(new Topic(topicId, "", ""));
        courseService.updateCourse(course);
        return "redirect:/topics/" + topicId + "/courses";
    }

    @RequestMapping(method= RequestMethod.DELETE, value="/topics/{topicId}/courses/{id}")
    public String deleteCourse(@PathVariable String id, @PathVariable String topicId) {
        courseService.deleteCourse(id);
        return "redirect:/topics/" + topicId + "/courses";
    }
}