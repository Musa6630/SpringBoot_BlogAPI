package com.myblog.controller;


import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.service.PostService;
import com.sun.deploy.net.proxy.MDefaultBrowserProxyConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        PostDto dto = postService.createPost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post is Deleted", HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        PostDto postDto = postService.getPostById(id);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable long id,
            @RequestBody PostDto postDto
    ) {
        PostDto dto = postService.updatePost(id, postDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    // http://localhost:8080/api/posts?pageNo=0&pageSize=5&sortBy=description&sortDir=desc
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name="pageSize", required = false, defaultValue = "5") int pageSize,
            @RequestParam(value="sortBy", defaultValue="id", required=false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PostResponse response = postService.getAllPosts(pageNo,pageSize, sortBy, sortDir);
        return response;

    }
}