package pl.romanek.blog.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import pl.romanek.blog.dto.PostRequestDto;
import pl.romanek.blog.dto.PostResponseDto;
import pl.romanek.blog.entity.Post;
import pl.romanek.blog.mapper.PostResponseMapper;
import pl.romanek.blog.mapper.PostRequestMapper;
import pl.romanek.blog.service.PostService;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRequestMapper postRequestMapper;
    private final PostResponseMapper postResponseMapper;

    @GetMapping("/all/{page}")
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@PathVariable("page") Integer page) {
        List<Post> posts = new ArrayList<>(postService.findAllPosts(page).toList());
        try {
            posts.stream().flatMap(item -> item.getComment().stream().map(p -> p.getImg().toString()))
                    .forEach(p -> System.out.println(p));
        } catch (Exception e) {
            System.out.println("NULL");
        }

        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(postResponseMapper.toPostsResponseDto(posts));
    }

    @GetMapping("/user/{id}/{page}")
    public ResponseEntity<List<PostResponseDto>> getAllPostByUserId(@PathVariable("id") Integer id,
            @PathVariable("page") Integer page) {
        List<Post> posts = new ArrayList<>(postService.findAllPostsByUserId(id, page).toList());
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(postResponseMapper.toPostsResponseDto(posts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable("id") Integer id) {
        Optional<Post> post = postService.findPostById(id);
        return ResponseEntity.of(Optional.ofNullable(postResponseMapper.toPostResponseDto(post.get())));
    }

    @PostMapping("/add")
    public ResponseEntity<PostResponseDto> addPost(@RequestBody PostRequestDto postRequestDto,
            Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getPrincipal().toString());
        Post post = postService.addPost(postRequestMapper.toPostEntity(postRequestDto),
                userId);
        return ResponseEntity.ok(postResponseMapper.toPostResponseDto(post));
    }

    @PostMapping(path = "/img/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PostResponseDto> addPostImg(@RequestParam("file") MultipartFile file,
            @RequestParam("id") Integer id,
            Authentication authentication) throws IOException {

        Integer userId = Integer.parseInt(authentication.getPrincipal().toString());
        Optional<Post> post = postService.findPostById(id);

        if (post.isPresent()) {
            post.get().setImg(file.getBytes());
            postService.addPost(post.get(), userId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
