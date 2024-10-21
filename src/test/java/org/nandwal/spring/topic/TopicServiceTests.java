package org.nandwal.spring.topic;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TopicServiceTests {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    public TopicServiceTests() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTopics() {
        List<Topic> topics = List.of(new Topic("1", "Topic 1", "Description 1"), new Topic("2", "Topic 2", "Description 2"));
        when(topicRepository.findAll()).thenReturn(topics);

        List<Topic> result = topicService.getAllTopics();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(topicRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTopics1() {
        when(topicRepository.findAll()).thenReturn(List.of());

        List<Topic> result = topicService.getAllTopics();
        assertNull(result);
        verify(topicRepository, times(1)).findAll();
    }

    @Test
    void testGetTopic() {
        Topic topic = new Topic("1", "Topic 1", "Description 1");
        when(topicRepository.findById("1")).thenReturn(Optional.of(topic));

        Topic result = topicService.getTopic("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(topicRepository, times(1)).findById("1");
    }

    @Test
    void testGetTopic1() {
        when(topicRepository.findById("1")).thenReturn(Optional.empty());

        Topic result = topicService.getTopic("1");
        assertNull(result);
        verify(topicRepository, times(1)).findById("1");
    }

    @Test
    void testAddTopic() {
        Topic topic = new Topic("1", "Topic 1", "Description 1");

        topicService.addTopic(topic);
        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    void testAddTopicException() {
        Topic topic = new Topic("1", "html", "Description for HTML");
        when(topicRepository.save(topic)).thenReturn(topic);
//        topicService.addTopic(topic);
        assertThrows(RuntimeException.class, () -> topicService.addTopic(topic));
        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    void testUpdateTopic() {
        Topic topic = new Topic("1", "Topic 1", "Description 1");

        topicService.updateTopic(topic, "1");
        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    void testDeleteTopic() {
        topicService.deleteTopic("1");
        verify(topicRepository, times(1)).deleteById("1");
    }
}