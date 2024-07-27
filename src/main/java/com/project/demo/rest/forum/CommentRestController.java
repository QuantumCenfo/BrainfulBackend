package com.project.demo.rest.forum;

import com.project.demo.logic.entity.comment.Comment;
import com.project.demo.logic.entity.comment.CommentRepository;
import com.project.demo.logic.entity.forum.Forum;
import com.project.demo.logic.entity.forum.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentRestController {
    @Autowired
    private CommentRepository commentRepository;

    // Get de todos los comentarios de un foro
    @GetMapping("/{id}")
    public List<Comment> GetAllComments(@PathVariable Long id) {
        return commentRepository.findByForumForumId(id);
    }

    // Insert de comentario en un foro
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public Comment addComment(@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }


    // Borra un comentario de unn foro, este funciona siempre
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public void deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
    }

    // Edita un comentario
    @PutMapping("/{id}")
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