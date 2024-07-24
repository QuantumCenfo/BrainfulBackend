package com.project.demo.rest.forum;

import com.project.demo.logic.entity.forum.Forum;
import com.project.demo.logic.entity.forum.ForumRepository;

import com.project.demo.logic.entity.comment.Comment;
import com.project.demo.logic.entity.comment.CommentRepository;

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

    // Get de todos los comentarios de un foro
    @GetMapping("ForumId/{id}")
    public List<Comment> GetAllComments(@PathVariable Long id) {
        return commentRepository.findByForumForumId(id);
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

    // Insert de comentario en un foro
    @PostMapping("ForumId/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public Comment addComment(@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }

    // Borra un foro y todos los comentarios que tenga ligado este mismo
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public void deleteForum(@PathVariable Long id) {
        commentRepository.deleteByForumForumId(id);
        forumRepository.deleteById(id);
    }

    // Borra un comentario de unn foro, este funciona siempre
    @DeleteMapping("ForumId/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public void deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
    }

    // Edita un comentario
    @PutMapping("ForumId/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        return commentRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setContent(comment.getContent());
                    existingUser.setAnonymous(comment.getAnonymous());
                    return commentRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    comment.setCommentId(id);
                    return commentRepository.save(comment);
                });
    }

}