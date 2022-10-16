package com.travula.repository;

import com.travula.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationVoteRepository extends JpaRepository<VerificationToken,Long> {
}