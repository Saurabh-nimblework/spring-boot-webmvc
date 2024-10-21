package org.nandwal.spring.topic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TopicController.class)
public class TopicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicService;

    @Test
    void testGetAllTopics() throws Exception {
        // use List.of instead of List.of
        List<Topic> topics = List.of(new Topic("1", "Topic 1", "Description 1"), new Topic("2", "Topic 2", "Description 2"));
        when(topicService.getAllTopics()).thenReturn(topics);

        mockMvc.perform(get("/topics"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("topics", topics))
                .andExpect(view().name("topics"));

        verify(topicService, times(1)).getAllTopics();

    }

    @Test
    void testGetAllTopics1() throws Exception {
        when(topicService.getAllTopics()).thenReturn(null);

        mockMvc.perform(get("/topics"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("topicNotFound", true))
                .andExpect(view().name("topics"));

        verify(topicService, times(1)).getAllTopics();
    }

    @Test
    void testGetTopic() throws Exception {
        Topic topic = new Topic("1", "Topic 1", "Description 1");
        when(topicService.getTopic("1")).thenReturn(topic);

        mockMvc.perform(get("/topics/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("topics", topic))
                .andExpect(view().name("topics"));

        verify(topicService, times(1)).getTopic("1");

    }

    @Test
    void testGetTopic1() throws Exception {
        when(topicService.getTopic("1")).thenReturn(null);

        mockMvc.perform(get("/topics/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("topicNotFound", true))
                .andExpect(view().name("topics"));

        verify(topicService, times(1)).getTopic("1");
    }

    @Test
    void testAddTopic() throws Exception {
        Topic topic = new Topic("1", "Topic 1", "Description 1");

        mockMvc.perform(post("/topics")
                        .contentType("application/json")
                        .content("{\"id\":\"1\",\"name\":\"Topic 1\",\"description\":\"Description 1\"}"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics"));

        verify(topicService, times(1)).addTopic(any(Topic.class));
    }

    @Test
    void testUpdateTopic() throws Exception {
        Topic topic = new Topic("1", "Topic 1", "Description 1");

        mockMvc.perform(put("/topics/1")
                        .contentType("application/json")
                        .content("{\"id\":\"1\",\"name\":\"Topic 1\",\"description\":\"Description 1\"}"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics"));

        verify(topicService, times(1)).updateTopic(any(Topic.class), eq("1"));
    }

    @Test
    void testDeleteTopic() throws Exception {
        mockMvc.perform(delete("/topics/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics"));

        verify(topicService, times(1)).deleteTopic("1");
    }
}