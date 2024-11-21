package org.nandwal.spring.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Transactional(readOnly = true)
    public List<Topic> getAllTopics() {
        List<Topic> topics = new ArrayList<>();
        topicRepository.findAll().forEach(topics::add);
        return topics.isEmpty() ? null : topics;
    }

    @Transactional(readOnly = true)
    public Topic getTopic(String id) {
        return topicRepository.findById(id).orElse(null);
    }

    // check for runtime exeption on stack overflow
    // use @Valid annotation to validate the input and Entity lifecycle   
    @Transactional
    public void addTopic(Topic topic) {
        topicRepository.save(topic);
        if(topic.getName().equals("html")) {
            throw new RuntimeException("Exception thrown from addTopic for topic name html");
//            throw new DataAccessException("Exception thrown from addTopic for topic name html") {
//                @Override
//                public String getMessage() {
//                    return super.getMessage();
//                }
//            };
        }
    }

    @Transactional
    public void updateTopic(Topic topic, String id) {
        topicRepository.save(topic);
    }

    @Transactional
    public void deleteTopic(String id) {
        topicRepository.deleteById(id);
    }
}
