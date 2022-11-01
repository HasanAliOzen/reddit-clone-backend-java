package com.travula.dto;

import com.travula.model.VoteType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class VoteDto {
    private VoteType voteType;
    private Long postId;
}
