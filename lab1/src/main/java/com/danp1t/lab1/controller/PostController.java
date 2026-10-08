package com.danp1t.lab1.controller;

import com.danp1t.lab1.dto.ResponseAccount;
import com.danp1t.lab1.dto.ResponsePost;
import com.danp1t.lab1.model.Post;
import com.danp1t.lab1.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/data")
    public List<ResponsePost> getPosts() {
        ArrayList<ResponsePost> posts = new ArrayList<>();
        for(Post post : postService.getPost()){
            posts.add(new ResponsePost(post.getId(), post.getTitle(),
                    post.getText(), post.getCreatedAt(),
                    new ResponseAccount(post.getOwner().getId(), post.getOwner().getLogin())));
        }
        return posts;
    }

//    @PostMapping("/add_post")
//    public Post addPost(@RequestBody RequestPost postDTO) {
//
//        //Нужно получить RequestAccount -> Account
//        Post post = new Post(postDTO.getTitle(), postDTO.getText(), new Account(postDTO.getOwner()));
//        return postService.savePost(post);
//    }
}
