package com.danp1t.lab1.repository;

import com.danp1t.lab1.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends  JpaRepository<Post, Long>{}
