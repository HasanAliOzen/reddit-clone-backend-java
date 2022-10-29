package com.travula.service;

import com.travula.dto.CommentDto;
import com.travula.exceptions.SpringRedditException;
import com.travula.model.Comment;
import com.travula.model.NotificationEmail;
import com.travula.model.Post;
import com.travula.model.User;
import com.travula.repository.CommentRepository;
import com.travula.repository.PostRepository;
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
public class CommentService {
    private static final String POST_URL = "";
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Transactional
    public void creteComment(CommentDto commentDto) {
        if (commentDto.getPostId() == null || commentDto.getText() == null){
            throw new SpringRedditException("Post id and comment text is required!!!");
        }
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(()-> new ProviderNotFoundException("Post id " +commentDto.getPostId()+ " not exist!!!"));

        Comment comment = map(commentDto,post,authService.getCurrentUser());
        comment.setCreatedDate(Instant.now());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(comment.getUser().getUsername() + " posted a comment on your post " + POST_URL);
        sendCommentNotification(message,post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(
                new NotificationEmail(
                        user.getUsername()+" commented on your post",
                        user.getEmail(),
                        message));
    }

    private Comment map(CommentDto commentDto,Post post, User user) {
        if (commentDto == null){
            return null;
        }

        return Comment.builder()
                .post(post)
                .user(user)
                .text(commentDto.getText())
                .build();
    }

    private CommentDto mapToDto(Comment comment){
        if (comment == null){
            return null;
        }

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .postId(comment.getPost().getId())
                .username(comment.getUser().getUsername())
                .createdDate(comment.getCreatedDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentDto getComment(Long id) {
        return mapToDto(commentRepository
                .findById(id)
                .orElseThrow(
                        ()-> new ProviderNotFoundException("No Comment id "+id)));
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsByPost(Long postId) {
        return commentRepository
                .findByPost(
                        postRepository.findById(postId)
                                .orElseThrow(
                                        () -> new ProviderNotFoundException("No Post with id " +postId)))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsByUsername(String username) {
        return commentRepository
                .findByUser(
                        userRepository
                                .findByUsername(username)
                                .orElseThrow(
                                        ()-> new ProviderNotFoundException("No user with username: " +username)))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
