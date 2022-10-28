package com.travula.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.LAZY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
@Table(name = "post")
public class Post extends BaseEntity{

    @NotBlank(message = "Post Name cannot be empty or Null!!!")
    @Column(name = "post_name",nullable = false)
    private String postName;

    @Nullable
    @Column(name = "url")
    private String url;

    @Nullable
    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "vote_count")
    private Integer voteCount;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "subreddit_id",referencedColumnName = "id")
    private Subreddit subreddit;

}
