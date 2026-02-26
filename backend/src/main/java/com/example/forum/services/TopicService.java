package com.example.forum.services;

import com.example.forum.entities.models.Category;
import com.example.forum.entities.models.Topic;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.CategoryRepository;
import com.example.forum.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TopicService {

    @Autowired private TopicRepository topicRepository;
    @Autowired private CategoryRepository categoryRepository;

    public List<Topic> getTopicsByCategory(String slug) { return topicRepository.findByCategorySlug(slug); }

    public Topic getTopicById(Integer id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        topic.setViews(topic.getViews() + 1);
        return topicRepository.save(topic);
    }

    public Topic createTopic(Topic topicRequest, User currentUser) {
        topicRequest.setUser(currentUser);
        if (topicRequest.getCategory() != null && topicRequest.getCategory().getSlug() != null) {
            Category category = categoryRepository.findBySlug(topicRequest.getCategory().getSlug())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
            topicRequest.setCategory(category);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category slug is required");
        }
        return topicRepository.save(topicRequest);
    }

    public Topic updateTopic(Integer id, Topic topicDetails, User currentUser) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        if (!topic.getUser().getUser_id().equals(currentUser.getUser_id()) && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: No tienes permiso para editar este hilo");
        }
        topic.setTitle(topicDetails.getTitle());
        topic.setContent(topicDetails.getContent());
        if (topicDetails.getCategory() != null && topicDetails.getCategory().getSlug() != null) {
            categoryRepository.findBySlug(topicDetails.getCategory().getSlug()).ifPresent(topic::setCategory);
        }
        return topicRepository.save(topic);
    }

    public void deleteTopic(Integer id, User currentUser) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        if (!topic.getUser().getUser_id().equals(currentUser.getUser_id()) && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: No tienes permiso para eliminar este hilo");
        }
        topicRepository.delete(topic);
    }
}
