package com.danp1t.lab1.controller;

import com.danp1t.lab1.model.Post;
import com.danp1t.lab1.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/data")
    public List<Post> getPosts() {
        return postService.getPost();
    }

    @PostMapping("/api/add_post")
    public void addPost(@RequestBody Post post) {
        postService.savePost(post);
    }
}
