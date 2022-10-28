package com.travula.controller;

import com.travula.dto.PostRequest;
import com.travula.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){
        postService.createPost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostRequest> getPost(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping
    public ResponseEntity<List<PostRequest>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostRequest>> getPostsBySubreddit(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostsBySubreddit(id));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<List<PostRequest>> getPostsByUser(@PathVariable String username){
        return ResponseEntity.ok(postService.getPostsByUsername(username));
    }

}
