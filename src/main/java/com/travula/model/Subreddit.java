package com.travula.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
@Table(name = "sub_reddit")
public class Subreddit extends BaseEntity{
    @NotBlank(message = "Community name is required!!!")
    @Column(name = "name",nullable = false)
    private String name;

    @NotBlank(message = "Description is required!!!")
    @Column(name = "description",nullable = false)
    private String description;

    @OneToMany(fetch = LAZY)
    private List<Post> posts;

    @ManyToOne(fetch = LAZY)
    private User user;
}
