package com.travula.service;

import com.travula.dto.PostRequest;
import com.travula.exceptions.SpringRedditException;
import com.travula.model.Post;
import com.travula.model.Subreddit;
import com.travula.model.User;
import com.travula.repository.PostRepository;
import com.travula.repository.SubredditRepository;
import com.travula.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.ProviderNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public void createPost(PostRequest postRequest) {
        if (postRequest.getPostName().isBlank()){
            throw  new SpringRedditException("Post name is a must!!!");
        }

        Subreddit subreddit =subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(()-> new ProviderNotFoundException("No Subreddit with name " + postRequest.getSubredditName()));

        Post save = map(postRequest,subreddit,authService.getCurrentUser());
        save.setCreatedDate(Instant.now());
        Post post = postRepository.save(save);
        subreddit.getPosts().add(post);
        subredditRepository.save(subreddit);
    }

    @Transactional(readOnly = true)
    public List<PostRequest> getAllPosts() {
        return postRepository.findAll()
                .stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Post map(PostRequest postRequest, Subreddit subreddit, User user) {
        if (postRequest == null){
            return null;
        }

        return Post.builder()
                .postName(postRequest.getPostName())
                .url(postRequest.getUrl())
                .description(postRequest.getDescription())
                .user(user)
                .subreddit(subreddit)
                .voteCount(0)
                .build();
    }

    private PostRequest mapToDto(Post post){
        if (post == null){
            return null;
        }

        return PostRequest.builder()
                .postId(post.getId())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .subredditName(post.getSubreddit().getName())
                .build();
    }

    @Transactional(readOnly = true)
    public PostRequest getPost(Long id) {
        return mapToDto(postRepository.findById(id)
                .orElseThrow(()-> new ProviderNotFoundException("No post with id " + id)));
    }

    @Transactional(readOnly = true)
    public List<PostRequest> getPostsBySubreddit(Long id) {
        List<Post> postList = postRepository
                .findAllBySubreddit(
                        subredditRepository.findById(id)
                                .orElseThrow(
                                        ()-> new ProviderNotFoundException("No subreddit with id " + id)));

        return postList
                .stream()
                .map(this::mapToDto).toList();
    }

    @Transactional(readOnly = true)
    public List<PostRequest> getPostsByUsername(String username) {
        return postRepository
                .findAllByUser(
                        userRepository.findByUsername(username)
                                .orElseThrow(
                                        ()-> new ProviderNotFoundException("No user with username " + username)))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
