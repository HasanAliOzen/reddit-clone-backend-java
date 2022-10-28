package com.travula.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SubredditDto {
    private Long id;
    private String subredditName;
    private String description;
    private Integer numberOfPosts;
}
