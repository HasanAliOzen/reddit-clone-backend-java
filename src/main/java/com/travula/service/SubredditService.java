package com.travula.service;

import com.travula.dto.SubredditDto;
import com.travula.exceptions.AlreadyExistException;
import com.travula.model.Subreddit;
import com.travula.repository.SubredditRepository;
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
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;


    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        if(subredditRepository.existsByName(subredditDto.getSubredditName())){
            throw new AlreadyExistException("Subreddit name " + subredditDto.getSubredditName() + " taken!!!");
        }

        Subreddit save = mapSubredditDto(subredditDto);
        save.setCreatedDate(Instant.now());
        save.setUser(authService.getCurrentUser());
        save = subredditRepository.save(save);
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        if (subredditDto == null){
            return null;
        }

        return Subreddit.builder()
                .name(subredditDto.getSubredditName())
                .description(subredditDto.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAllSubreddits() {
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        if (subreddit == null){
            return null;
        }

        return SubredditDto.builder()
                .id(subreddit.getId())
                .subredditName(subreddit.getName())
                .description(subreddit.getDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        return mapToDto(subredditRepository.findById(id).orElseThrow(
                ()-> new ProviderNotFoundException("No Subreddit with id " + id)));
    }

}
