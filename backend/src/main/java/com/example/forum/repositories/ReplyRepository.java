package com.example.forum.repositories;
import com.example.forum.entities.models.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findByTopicId(Integer topicId);
}
