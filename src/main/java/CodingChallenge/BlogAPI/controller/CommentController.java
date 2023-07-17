package CodingChallenge.BlogAPI.controller;

import CodingChallenge.BlogAPI.dto.CommentDTO;
import CodingChallenge.BlogAPI.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor

public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping(path = "/createComment/{id}/comments",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<CommentDTO> createComment(
            @PathVariable(name = "id") long postId,
            @RequestBody CommentDTO commentDTO
    ){
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/getAllComments/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getAllPostComments(
            @PathVariable(name = "id") long postId
    ){
        return new ResponseEntity<>(commentService.getAllPostComments(postId), HttpStatus.OK);
    }

    @GetMapping("/getComment/{id}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(
            @PathVariable(name = "id") long postId,
            @PathVariable(name = "commentId") long commentId
    ){
        return new ResponseEntity<>(commentService.getCommentById(postId, commentId), HttpStatus.OK);
    }
    @PutMapping("/updateComment/{id}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable(name = "id") long postId,
            @PathVariable(name = "commentId") long commentId,
            @RequestBody CommentDTO commentDTO
    ){
        return new ResponseEntity<>(commentService.update(postId, commentId, commentDTO), HttpStatus.OK);
    }
    @DeleteMapping("/deleteComment/{id}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable(name = "id") long postId,
            @PathVariable(name = "commentId") long commentId
    ){
        return new ResponseEntity<>(commentService.deleteComment(postId, commentId), HttpStatus.OK);
    }
}
