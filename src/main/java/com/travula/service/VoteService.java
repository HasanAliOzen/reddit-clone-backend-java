package com.travula.service;

import com.travula.dto.VoteDto;
import com.travula.model.Post;
import com.travula.model.Vote;
import com.travula.model.VoteType;
import com.travula.repository.PostRepository;
import com.travula.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.ProviderNotFoundException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;


    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(()-> new ProviderNotFoundException("No Post with id: " + voteDto.getPostId()));
        Vote vote = voteRepository.findByPostAndUser(post,authService.getCurrentUser()).orElse(null);
        if (vote == null){
            newVote(post,voteDto.getVoteType());
            return;
        }
        if(!vote.getVoteType().equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()-vote.getVoteType().getDirection()+voteDto.getVoteType().getDirection());
            postRepository.save(post);
            vote.setVoteType(voteDto.getVoteType());
            voteRepository.save(vote);
        }
    }

    private void newVote(Post post, VoteType voteType) {
        Vote vote = new Vote();
        vote.setCreatedDate(Instant.now());
        vote.setPost(post);
        vote.setUser(authService.getCurrentUser());
        vote.setVoteType(voteType);
        voteRepository.save(vote);
        post.setVoteCount(post.getVoteCount() + vote.getVoteType().getDirection());
        postRepository.save(post);
    }
}
