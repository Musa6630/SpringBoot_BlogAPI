package com.myblog.service;

import com.myblog.payload.PostDto;

public interface PostService {

    PostDto savePost(PostDto postDto);
}
