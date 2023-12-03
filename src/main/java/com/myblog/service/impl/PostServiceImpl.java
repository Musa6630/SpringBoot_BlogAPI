package com.myblog.service.impl;

import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFountException;
import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.repository.PostRepository;
import com.myblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepo;
    private ModelMapper modelMapper;
    public PostServiceImpl(PostRepository postRepo, ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.modelMapper=modelMapper;
    }


    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post savedPost=postRepo.save(post);
        PostDto dto=mapToDto(savedPost);
        return dto;
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFountException("Post Not Found:" + id)
        );
        postRepo.deleteById(id);

    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFountException("Post not found:" + id)
        );
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFountException("Post not found:" + id)
        );

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post SavedPost = postRepo.save(post);
        PostDto dto = mapToDto(SavedPost);
        return dto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
//        Pageable pageable= PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
       // Pageable pageable= PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC,sortBy));

        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);

        Page<Post> pagePostObjects = postRepo.findAll(pageable);
        List<Post> posts = pagePostObjects.getContent();

        List<PostDto> dtos=posts.stream().map(post->mapToDto(post)).collect(Collectors.toList());

        PostResponse response=new PostResponse();
        response.setDto(dtos);
        response.setPageNo(pagePostObjects.getNumber());
        response.setTotalPages(pagePostObjects.getTotalPages());
        response.setLastPage(pagePostObjects.isLast());
        response.setPageSize(pagePostObjects.getSize());

        return response;
    }


    PostDto mapToDto(Post savedPost) {
//        PostDto postDto=new PostDto();
        PostDto postDto = modelMapper.map(savedPost, PostDto.class);
//        postDto.setId(savedPost.getId());
//        postDto.setTitle(savedPost.getTitle());
//        postDto.setContent(savedPost.getContent());
//        postDto.setDescription(savedPost.getDescription());
        return postDto;

    }

    Post mapToEntity(PostDto postDto){
//        Post post=new Post();
        Post post = modelMapper.map(postDto, Post.class);
//        post.setTitle(postDto.getTitle());
//        post.setContent(postDto.getContent());
//        post.setDescription(postDto.getDescription());
        return post;
    }
}