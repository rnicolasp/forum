package com.example.forum.controllers;

import com.example.forum.entities.models.Topic;
import com.example.forum.entities.models.User;
import com.example.forum.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TopicController {

    @Autowired
    private TopicService topicService;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/categories/{slug}/topics")
    public List<Topic> getTopicsByCategory(@PathVariable String slug) {
        return topicService.getTopicsByCategory(slug);
    }

    @GetMapping("/topics/{id}")
    public Topic getTopicById(@PathVariable Integer id) {
        return topicService.getTopicById(id);
    }

    @PostMapping("/topics")
    public Topic createTopic(@RequestBody Topic topicRequest) {
        return topicService.createTopic(topicRequest, getCurrentUser());
    }

    @PutMapping("/topics/{id}")
    public Topic updateTopic(@PathVariable Integer id, @RequestBody Topic topicDetails) {
        return topicService.updateTopic(id, topicDetails, getCurrentUser());
    }

    @DeleteMapping("/topics/{id}")
    public void deleteTopic(@PathVariable Integer id) {
        topicService.deleteTopic(id, getCurrentUser());
    }
}
