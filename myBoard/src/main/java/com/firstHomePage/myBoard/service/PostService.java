package com.firstHomePage.myBoard.service;

import com.firstHomePage.myBoard.domain.Post;
import com.firstHomePage.myBoard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long save(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    public Post findOne(Long postId) {
        return postRepository.findOne(postId);
    }

    public List<Post> findAll(int page, int size) {
        return postRepository.findAll(page, size);
    }

    @Transactional
    public void update(Long id, String title, String contents) {
        Post post = postRepository.findOne(id);

        String newTitle = Optional.ofNullable(title).orElse(post.getTitle());
        String newContent = Optional.ofNullable(contents).orElse(post.getContents());

        post.setTitle(newTitle);
        post.setContents(newContent);

        postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findOne(id);
        postRepository.delete(post);
    }

    public List<Post> findAllByKeyword(String keyword) {
        return postRepository.findAllByKeyword(keyword);
    }

    public List<Post> findAllByNickname(String nickname) {
        return postRepository.findAllByNickname(nickname);
    }
}
