package br.com.Badrequest.ajudae.api.controller;

import br.com.Badrequest.ajudae.api.dto.CreatePost;
import br.com.Badrequest.ajudae.api.dto.PostDto;
import br.com.Badrequest.ajudae.api.mapper.PostMapper;
import br.com.Badrequest.ajudae.api.model.Post;
import br.com.Badrequest.ajudae.api.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    public PostController(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> findAll(){
        List<PostDto> posts = this.postService.findAll()
                .stream()
                .map(this.postMapper::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> findById(@PathVariable Long id){
        Post post = this.postService.findById(id);
        return new ResponseEntity<>(this.postMapper.convertToDto(post), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@RequestBody CreatePost dto){
        Post post = this.postService.create(this.postMapper.convertToEntity(dto));
        return new ResponseEntity<>(this.postMapper.convertToDto(post), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@PathVariable Long id, @RequestBody PostDto dto){
        Post post = this.postService.findById(id);
        post.setDescricao(dto.getDescricao());
        post.setTitulo(dto.getTitulo());
        post.setId_user(dto.getId_user());
        return new ResponseEntity<>(this.postMapper.convertToDto(this.postService.update(post)), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        Post post = this.postService.findById(id);
        this.postService.delete(post);
        return new ResponseEntity<>("Deletado com sucesso", HttpStatus.OK);
    }
}
