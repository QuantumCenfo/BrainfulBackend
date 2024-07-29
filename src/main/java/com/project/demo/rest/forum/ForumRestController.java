package com.project.demo.rest.forum;

import com.project.demo.logic.entity.forum.Forum;
import com.project.demo.logic.entity.forum.ForumRepository;

import com.project.demo.logic.entity.comment.Comment;
import com.project.demo.logic.entity.comment.CommentRepository;

import com.project.demo.logic.entity.game.Game;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forums")
public class ForumRestController {
    @Autowired
    private ForumRepository forumRepository;
    @Autowired
    private CommentRepository commentRepository;

    // Get de todos los foros
    @GetMapping
    public List<Forum> GetAllForums() {
        return forumRepository.findAll();
    }

    @GetMapping("/{id}")
    public Forum getForumbyId(@PathVariable Long id) {
        return forumRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    // Get de todos los foros de un usuario
    @GetMapping("UserId/{id}")
    public List<Forum> GetAllUserForums(@PathVariable Long id) {
        return forumRepository.findByUserId(id);
    }


    // Insert de foro
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public Forum addForum(@RequestBody Forum forum) {
        return forumRepository.save(forum);
    }

    // Borra un foro y todos los comentarios que tenga ligado este mismo
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public void deleteForum(@PathVariable Long id) {
        commentRepository.deleteByForumForumId(id);
        forumRepository.deleteById(id);
    }

}