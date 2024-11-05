package org.nandwal.spring.topic;

import org.junit.jupiter.api.*;
import org.nandwal.spring.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class TopicServiceTests {

    @Nested
    @DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TopicService.class))
    class DataJpaTests {

        @Autowired
        private TopicRepository topicRepository;

        @Autowired
        private TopicService topicService;

        @Autowired
        private CourseRepository courseRepository;

        @BeforeEach
        void setUp() {
            courseRepository.deleteAll();
            topicRepository.deleteAll();
        }

        @Test
        void testGetAllTopics() {
            List<Topic> topics = List.of(new Topic("1a", "Topic 1", "Description 1"), new Topic("2a", "Topic 2", "Description 2"));
            topicRepository.saveAll(topics);

            List<Topic> result = topicService.getAllTopics();
            assertNotNull(result);
            assertEquals(2, result.size());
        }

        @Test
        void testGetTopic() {
            Topic topic = new Topic("1b", "Topic 1", "Description 1tGT");
            topicRepository.save(topic);

            Topic result = topicService.getTopic("1b");
            assertNotNull(result);
            assertEquals("1b", result.getId());
        }

        @Test
        void testAddTopic() {
            Topic topic = new Topic("1c", "Topic 1", "Description 1tAT");

            topicService.addTopic(topic);
            Optional<Topic> result = topicRepository.findById("1c");
            assertTrue(result.isPresent());
        }

        @Test
        void testUpdateTopic() {
            Topic topic = new Topic("1e", "Topic 1", "Description 1tUT");
            topicRepository.save(topic);

            topic.setName("Updated Topic 1");
            topicService.updateTopic(topic, "1e");
            Optional<Topic> result = topicRepository.findById("1e");
            assertTrue(result.isPresent());
            assertEquals("Updated Topic 1", result.get().getName());
        }

        @Test
        void testDeleteTopic() {
            Topic topic = new Topic("1f", "Topic 1", "Description 1tDT");
            topicRepository.save(topic);

            topicService.deleteTopic("1f");
            Optional<Topic> result = topicRepository.findById("1f");
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @SpringBootTest
    class SpringBootTests {

        @Autowired
        private TopicRepository topicRepository;

        @Autowired
        private TopicService topicService;

        @Autowired
        private CourseRepository courseRepository;

        @BeforeEach
        void setUp() {
            courseRepository.deleteAll();
            topicRepository.deleteAll();
        }

        @Test
        void testAddTopic_Transactional() {
            Topic topic = new Topic("1d", "html", "Description for HTML");
            assertThrows(RuntimeException.class, () -> topicService.addTopic(topic));
            Topic result = topicRepository.findById("1d").orElse(null);
            assertNull(result);
        }
    }
}