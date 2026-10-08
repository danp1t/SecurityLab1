package com.danp1t.lab1.controller;

import com.danp1t.lab1.dto.RequestPost;
import com.danp1t.lab1.dto.ResponseAccount;
import com.danp1t.lab1.dto.ResponsePost;
import com.danp1t.lab1.model.Account;
import com.danp1t.lab1.model.Post;
import com.danp1t.lab1.service.AccountService;
import com.danp1t.lab1.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final AccountService accountService;

    public PostController(PostService postService, AccountService accountService) {
        this.postService = postService;
        this.accountService = accountService;
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

    @PostMapping("/add_post")
    public ResponsePost addPost(@RequestBody RequestPost requestPost, Authentication authentication) {
        String login = authentication.getName();

        Account account = accountService.findByLogin(login);

        Post post = new Post(requestPost.getTitle(), requestPost.getText(), account);
        post = postService.savePost(post);

        return new ResponsePost(
                post.getId(),
                post.getTitle(),
                post.getText(),
                post.getCreatedAt(),
                new ResponseAccount(post.getOwner().getId(), post.getOwner().getLogin())
        );
    }
}
