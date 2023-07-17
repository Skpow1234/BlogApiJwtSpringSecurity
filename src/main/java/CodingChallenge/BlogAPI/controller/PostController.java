package CodingChallenge.BlogAPI.controller;

import CodingChallenge.BlogAPI.dto.PostDTO;
import CodingChallenge.BlogAPI.security.auth.response.PostResponse;
import CodingChallenge.BlogAPI.service.PostService;
import CodingChallenge.BlogAPI.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(path = "/createPost",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO){
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = AppConstants.DEFAULT_SORT_BY,required = false )String sort,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(postService.getAllPosts(pageNo, pageSize, sort, sortDir), HttpStatus.OK);
    }

    @GetMapping("/getPost/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable(name = "id") Long postId){
        return new ResponseEntity<>(postService.findPostById(postId),HttpStatus.OK);
    }

    @PutMapping("/updatePost/{id}")
    public ResponseEntity<PostDTO> updatePost(@Valid @RequestBody PostDTO postDTO, @PathVariable(name = "id") Long postId){
        return new ResponseEntity<>(postService.updatePost(postDTO, postId), HttpStatus.OK);
    }

    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long postId){
        return ResponseEntity.ok(postService.deletePostById(postId));
    }

    @GetMapping("/getPost/category/{id}")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(
            @PathVariable(name = "id") Long categoryId
    ){
        return new ResponseEntity<>(postService.getPostsByCategory(categoryId),HttpStatus.OK);
    }
}
