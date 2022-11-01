package com.travula.repository;

import com.travula.model.Post;
import com.travula.model.User;
import com.travula.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findByPostAndUser(Post post, User user);
}
