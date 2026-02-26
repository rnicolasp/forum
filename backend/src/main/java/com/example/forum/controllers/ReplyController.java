package com.example.forum.controllers;

import com.example.forum.entities.models.Reply;
import com.example.forum.entities.models.User;
import com.example.forum.services.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics/{topicId}/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public Reply addReply(@PathVariable Integer topicId, @RequestBody Reply replyRequest) {
        return replyService.addReply(topicId, replyRequest, getCurrentUser());
    }

    @PutMapping("/{replyId}")
    public Reply updateReply(@PathVariable Integer topicId, @PathVariable Integer replyId, @RequestBody Reply replyDetails) {
        return replyService.updateReply(topicId, replyId, replyDetails, getCurrentUser());
    }

    @DeleteMapping("/{replyId}")
    public void deleteReply(@PathVariable Integer topicId, @PathVariable Integer replyId) {
        replyService.deleteReply(topicId, replyId, getCurrentUser());
    }
}
