package com.travula.dto;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CommentDto {
    private Long id;
    private Long postId;
    private Instant createdDate;
    private String username;
    private String text;
}
