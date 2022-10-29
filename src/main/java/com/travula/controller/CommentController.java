package com.travula.controller;

import com.travula.dto.CommentDto;
import com.travula.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto){
        commentService.creteComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments(){
        return ResponseEntity.ok(commentService.getAllComments());

    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @GetMapping("/by-post/{id}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPost(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getAllCommentsByPost(id));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByUsername(@PathVariable String username){
        return ResponseEntity.ok(commentService.getAllCommentsByUsername(username));
    }
}
