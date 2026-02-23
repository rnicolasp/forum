package com.example.forum.controllers;
import com.example.forum.entities.models.Reply;
import com.example.forum.entities.models.Topic;
import com.example.forum.entities.models.User;
import com.example.forum.repositories.ReplyRepository;
import com.example.forum.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/api/topics/{topicId}/replies")
public class ReplyController {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private TopicRepository topicRepository;
    @PostMapping
    public ResponseEntity<?> addReply(@PathVariable Integer topicId, @RequestBody Reply replyRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Optional<Topic> topicOpt = topicRepository.findById(topicId);
        if (topicOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        replyRequest.setUser(currentUser);
        replyRequest.setTopic(topicOpt.get());
        Reply savedReply = replyRepository.save(replyRequest);
        return ResponseEntity.ok(savedReply);
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<?> updateReply(@PathVariable Integer topicId, @PathVariable Integer replyId, @RequestBody Reply replyDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Optional<Reply> replyOpt = replyRepository.findById(replyId);
        if (replyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reply reply = replyOpt.get();

        if (!reply.getTopic().getId().equals(topicId)) {
            return ResponseEntity.badRequest().body("La respuesta no pertenece a este hilo");
        }

        if (!reply.getUser().getUser_id().equals(currentUser.getUser_id()) 
                && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(403).body("No tienes permiso para editar esta respuesta");
        }

        reply.setContent(replyDetails.getContent());
        Reply updatedReply = replyRepository.save(reply);
        
        return ResponseEntity.ok(updatedReply);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable Integer topicId, @PathVariable Integer replyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Optional<Reply> replyOpt = replyRepository.findById(replyId);
        if (replyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reply reply = replyOpt.get();

        if (!reply.getTopic().getId().equals(topicId)) {
            return ResponseEntity.badRequest().body("La respuesta no pertenece a este hilo");
        }

        if (!reply.getUser().getUser_id().equals(currentUser.getUser_id()) 
                && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            return ResponseEntity.status(403).body("No tienes permiso para eliminar esta respuesta");
        }

        replyRepository.delete(reply);
        return ResponseEntity.ok().build();
    }
}
