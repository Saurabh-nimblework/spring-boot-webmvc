package org.nandwal.spring.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Async("customThreadPool")
    @Transactional(readOnly = true)
    public CompletableFuture<List<Topic>> getAllTopics() {
        try {
            List<Topic> topics = new ArrayList<>();
            topicRepository.findAll().forEach(topics::add);
            return CompletableFuture.completedFuture(topics);
        } catch ( DataAccessException e) {
            return CompletableFuture.failedFuture(null);
        }
    }

    @Async("customThreadPool")
    @Transactional(readOnly = true)
    public CompletableFuture<Topic> getTopic(String id) {
        try {
            Topic topic =  topicRepository.findById(id).orElse(null);
            return CompletableFuture.completedFuture(topic);
        } catch ( DataAccessException e) {
            return CompletableFuture.failedFuture(null);
        }
    }

    @Async("customThreadPool")
    @Transactional
    public CompletableFuture<Void> addTopic(Topic topic) {
        return CompletableFuture.runAsync(() -> {
            topicRepository.save(topic);
            if (topic.getName().equals("html")) {
                throw new RuntimeException("Exception thrown from addTopic for topic name html");
            }
        });
    }

    @Async("customThreadPool")
    @Transactional
    public CompletableFuture<Void> updateTopic(Topic topic, String id) {
        return CompletableFuture.runAsync(() -> {
            topicRepository.save(topic);
        });
    }

    @Async("customThreadPool")
    @Transactional
    public CompletableFuture<Void> deleteTopic(String id) {
        return CompletableFuture.runAsync(() -> {
            topicRepository.deleteById(id);
        });
    }
}
