package CodingChallenge.BlogAPI.service;

import CodingChallenge.BlogAPI.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(long postId, CommentDTO commentDTO);

    List<CommentDTO> getAllPostComments(long postId);

    CommentDTO getCommentById(long postId, long commentId);

    CommentDTO update(long postId, long commentId, CommentDTO commentDTO);

    String deleteComment(long postId, long commentId);
}
