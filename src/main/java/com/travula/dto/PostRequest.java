package com.travula.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class PostRequest {
    private Long postId;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
}
