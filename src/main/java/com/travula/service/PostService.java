package com.travula.service;

import com.travula.dto.PostRequest;
import com.travula.dto.PostResponse;
import com.travula.exceptions.SpringRedditException;
import com.travula.model.*;
import com.travula.repository.PostRepository;
import com.travula.repository.SubredditRepository;
import com.travula.repository.UserRepository;
import com.travula.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.ProviderNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    @Transactional
    public void createPost(PostRequest postRequest) {
        if (postRequest.getPostName().isBlank()) {
            throw new SpringRedditException("Post name is a must!!!");
        }

        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new ProviderNotFoundException(
                        "No Subreddit with name " + postRequest.getSubredditName()));

        Post save = mapRequest(postRequest, subreddit, authService.getCurrentUser());
        save.setCreatedDate(Instant.now());
        Post post = postRepository.save(save);
        subreddit.getPosts().add(post);
        subredditRepository.save(subreddit);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts() {
        return postRepository
                .findAll(Pageable.unpaged())
                .map(this::mapToResponse);
    }

    private Post mapRequest(PostRequest postRequest, Subreddit subreddit, User user) {
        if (postRequest == null) {
            return null;
        }

        return Post.builder()
                .postName(postRequest.getPostName())
                .url(postRequest.getUrl())
                .description(postRequest.getDescription())
                .user(user)
                .subreddit(subreddit)
                .voteCount(0)
                .commentCount(0)
                .build();
    }

    private PostResponse mapToResponse(Post post) {
        if (post == null) {
            return null;
        }
        Vote vote = voteRepository.findByPostAndUser(post, authService.getCurrentUser())
                .orElse(Vote.builder()
                        .voteType(VoteType.NOVOTE)
                        .build());

        return PostResponse.builder()
                .postId(post.getId())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .userName(post.getUser().getUsername())
                .subredditName(post.getSubreddit().getName())
                .voteCount(post.getVoteCount())
                .commentCount(post.getCommentCount())
                .upVote(vote.getVoteType().getDirection() == 1)
                .downVote(vote.getVoteType().getDirection() == -1)
                .build();
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        return mapToResponse(postRepository.findById(id)
                .orElseThrow(() -> new ProviderNotFoundException("No post with id " + id)));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long id) {
        List<Post> postList = postRepository
                .findAllBySubreddit(
                        subredditRepository.findById(id)
                                .orElseThrow(
                                        () -> new ProviderNotFoundException("No subreddit with id " + id)));

        return postList
                .stream()
                .map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        return postRepository
                .findAllByUser(
                        userRepository.findByUsername(username)
                                .orElseThrow(
                                        () -> new ProviderNotFoundException("No user with username " + username)))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
