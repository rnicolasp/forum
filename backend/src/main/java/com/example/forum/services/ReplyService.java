package com.example.forum.services;

import com.example.forum.entities.models.Reply;
import com.example.forum.entities.models.Topic;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.ReplyRepository;
import com.example.forum.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private TopicRepository topicRepository;

    public Reply addReply(Integer topicId, Reply replyRequest, User currentUser) {
        Topic topic = topicRepository.findById(topicId).orElseThrow();
        replyRequest.setUser(currentUser);
        replyRequest.setTopic(topic);
        return replyRepository.save(replyRequest);
    }

    public Reply updateReply(Integer topicId, Integer replyId, Reply replyDetails, User currentUser) {
        Reply reply = replyRepository.findById(replyId).orElseThrow();
        reply.setContent(replyDetails.getContent());
        return replyRepository.save(reply);
    }

    public void deleteReply(Integer topicId, Integer replyId, User currentUser) {
        Reply reply = replyRepository.findById(replyId).orElseThrow();
        replyRepository.delete(reply);
    }
}
