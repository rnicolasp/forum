package com.example.forum.repositories;
import com.example.forum.entities.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findByCategorySlug(String slug);
}
