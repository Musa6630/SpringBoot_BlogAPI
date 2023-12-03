package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFountException;
import com.myblog.payload.CommentDto;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import com.myblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepo;
    private PostRepository postRepo;

    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepo, PostRepository postRepo,ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.modelMapper=modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFountException("Post not Found with id: " + postId)
        );
        Comment comment = maptoEntity(commentDto);
        comment.setPost(post);
        Comment c = commentRepo.save(comment);
        return maptoDto(c);
    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFountException("Post not Found with id: " + postId)
        );

        commentRepo.deleteById(commentId);

    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
//        maptoDto(comments);
        List<CommentDto> dtos = comments.stream().map(comment -> maptoDto(comment)).collect(Collectors.toList());

        return dtos;
    }

    @Override
    public CommentDto updateComment(long commentId, CommentDto commentDto) {

        Comment com = commentRepo.findById(commentId).get();
        Post post = postRepo.findById(com.getId()).get();
        Comment comment = maptoEntity(commentDto);
        comment.setPost(post);
        comment.setId(commentId);
        Comment savedComment = commentRepo.save(comment);
        CommentDto dto = maptoDto(savedComment);
        return dto;
    }


    Comment maptoEntity(CommentDto dto){
//        Comment comment=new Comment();
        Comment comment = modelMapper.map(dto, Comment.class);
//        comment.setName(dto.getName());
//        comment.setEmail(dto.getEmail());
//        comment.setBody(dto.getBody());

        return comment;
    }
    CommentDto maptoDto(Comment comment){
//        CommentDto dto=new CommentDto();
        CommentDto dto = modelMapper.map(comment, CommentDto.class);
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setBody(comment.getBody());

        return dto;
    }
}
