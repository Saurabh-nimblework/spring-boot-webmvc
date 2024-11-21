package org.nandwal.spring.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TopicController {

    @Autowired
    private TopicService topicService;

    @RequestMapping("/topics")
    public String getAllTopics(Model model) {
        List<Topic> topics = topicService.getAllTopics();
        if(topics == null) {
            model.addAttribute("topicNotFound", true);
        } else {
            model.addAttribute("topics", topics);
        }
        return "topics";
    }

    @RequestMapping("/topics/{idx}")
    public String getTopic(@PathVariable("idx") String id, Model model) {
        Topic topic = topicService.getTopic(id);
        if(topic == null) {
            model.addAttribute("topicNotFound", true);
        } else {
            model.addAttribute("topics", topic);
        }
        return "topics";
    }

    @RequestMapping(method= RequestMethod.POST, value="/topics")
    public String addTopic(@RequestBody Topic topic) {
        topicService.addTopic(topic);
        return "redirect:/topics";
    }

    @RequestMapping(method= RequestMethod.PUT, value="/topics/{id}")
    public String updateTopic(@RequestBody Topic topic, @PathVariable String id, Model model) {
        topicService.updateTopic(topic, id);
        return "redirect:/topics";
    }

    @RequestMapping(method= RequestMethod.DELETE, value="/topics/{id}")
    public String deleteTopic(@PathVariable String id, Model model) {
        topicService.deleteTopic(id);
        return "redirect:/topics";
    }
}
