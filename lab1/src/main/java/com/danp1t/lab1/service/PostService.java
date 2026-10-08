package com.danp1t.lab1.service;

import com.danp1t.lab1.dto.ResponseAccount;
import com.danp1t.lab1.dto.ResponsePost;
import com.danp1t.lab1.model.Post;
import com.danp1t.lab1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getPost(){
        return postRepository.findAll();
    };

    public Post savePost(Post post){
        return postRepository.save(post);
    }
}
