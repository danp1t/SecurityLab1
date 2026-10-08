package com.danp1t.lab1.controller;

import com.danp1t.lab1.dto.RequestPost;
import com.danp1t.lab1.model.Account;
import com.danp1t.lab1.model.Post;
import com.danp1t.lab1.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/data")
    public List<Post> getPosts() {
        return postService.getPost();
    }

//    @PostMapping("/add_post")
//    public Post addPost(@RequestBody RequestPost postDTO) {
//
//        //Нужно получить RequestAccount -> Account
//        Post post = new Post(postDTO.getTitle(), postDTO.getText(), new Account(postDTO.getOwner()));
//        return postService.savePost(post);
//    }
}
