package com.travula.repository;

import com.travula.model.Post;
import com.travula.model.Subreddit;
import com.travula.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findAllByUser (User user);
}
