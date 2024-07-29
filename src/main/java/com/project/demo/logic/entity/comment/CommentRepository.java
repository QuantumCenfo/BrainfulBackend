package com.project.demo.logic.entity.comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByForumForumId(Long forumId);
    void deleteByForumForumId(Long forumId);
}