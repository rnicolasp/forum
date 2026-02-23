package com.example.forum.controllers;
import com.example.forum.entities.models.Category;
import com.example.forum.entities.models.Topic;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.CategoryRepository;
import com.example.forum.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping("/categories/{slug}/topics")
    public ResponseEntity<List<Topic>> getTopicsByCategory(@PathVariable String slug) {
        List<Topic> topics = topicRepository.findByCategorySlug(slug);
        return ResponseEntity.ok(topics);
    }
    @GetMapping("/topics/{id}")
    public ResponseEntity<?> getTopicById(@PathVariable Integer id) {
        Optional<Topic> topic = topicRepository.findById(id);
        if (topic.isPresent()) {
            Topic t = topic.get();
            t.setViews(t.getViews() + 1);
            topicRepository.save(t);
            return ResponseEntity.ok(t);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/topics")
    public ResponseEntity<?> createTopic(@RequestBody Topic topicRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        topicRequest.setUser(currentUser);
        if (topicRequest.getCategory() != null && topicRequest.getCategory().getSlug() != null) {
             Optional<Category> category = categoryRepository.findBySlug(topicRequest.getCategory().getSlug());
             if (category.isEmpty()) {
                 return ResponseEntity.badRequest().body("Category not found");
             }
             topicRequest.setCategory(category.get());
        } else {
             return ResponseEntity.badRequest().body("Category slug is required");
        }
        Topic savedTopic = topicRepository.save(topicRequest);
        return ResponseEntity.ok(savedTopic);
    }
    @PutMapping("/topics/{id}")
    public ResponseEntity<?> updateTopic(@PathVariable Integer id, @RequestBody Topic topicDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Optional<Topic> topicOpt = topicRepository.findById(id);
        if (topicOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Topic topic = topicOpt.get();
        if (!topic.getUser().getUser_id().equals(currentUser.getUser_id()) 
                && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(403).body("No tienes permiso para editar este hilo");
        }
        topic.setTitle(topicDetails.getTitle());
        topic.setContent(topicDetails.getContent());
        if (topicDetails.getCategory() != null && topicDetails.getCategory().getSlug() != null) {
            Optional<Category> catOpt = categoryRepository.findBySlug(topicDetails.getCategory().getSlug());
            catOpt.ifPresent(topic::setCategory);
        }
        Topic updatedTopic = topicRepository.save(topic);
        return ResponseEntity.ok(updatedTopic);
    }
    @DeleteMapping("/topics/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Optional<Topic> topicOpt = topicRepository.findById(id);
        if (topicOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Topic topic = topicOpt.get();
        if (!topic.getUser().getUser_id().equals(currentUser.getUser_id()) 
                && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(403).body("No tienes permiso para eliminar este hilo");
        }
        topicRepository.delete(topic);
        return ResponseEntity.ok().build();
    }
}
