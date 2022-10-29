package com.travula.repository;

import com.travula.model.Comment;
import com.travula.model.Post;
import com.travula.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findByUser(User user);
}
