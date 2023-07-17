package CodingChallenge.BlogAPI.service.impl;

import CodingChallenge.BlogAPI.domain.Comment;
import CodingChallenge.BlogAPI.domain.Post;
import CodingChallenge.BlogAPI.domain.User;
import CodingChallenge.BlogAPI.dto.CommentDTO;
import CodingChallenge.BlogAPI.exception.APIException;
import CodingChallenge.BlogAPI.exception.ResourceNotFoundException;
import CodingChallenge.BlogAPI.repository.CommentRepository;
import CodingChallenge.BlogAPI.repository.PostRepository;
import CodingChallenge.BlogAPI.repository.UserRepository;
import CodingChallenge.BlogAPI.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    @Override
    public CommentDTO createComment(long postId, CommentDTO commentDTO) {
        Comment comment = mapToEntity(commentDTO);
        Post post = postRepository.findById(postId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Post", "id", postId));
        User user = userRepository.findByEmail(authanticatedUsername()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        comment.setPost(post);
        comment.setCommentUser(user);
        Comment responseComment = commentRepository.save(comment);
        return mapToDto(responseComment);
    }

    @Override
    public List<CommentDTO> getAllPostComments(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            new APIException(HttpStatus.BAD_REQUEST, "This comment not belong to post.");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDTO update(long postId, long commentId, CommentDTO commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            new APIException(HttpStatus.BAD_REQUEST, "This comment not belong to post.");
        }
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment responseComment = commentRepository.save(comment);
        return mapToDto(responseComment);
    }

    @Override
    public String deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            new APIException(HttpStatus.BAD_REQUEST, "This comment not belong to post.");
        }
        commentRepository.deleteById(comment.getId());
        return "Comment successfully deleted.";
    }

    private CommentDTO mapToDto(Comment comment){
        CommentDTO commentDTO = modelMapper.map(comment,CommentDTO.class);
        return commentDTO;
    }

    private Comment mapToEntity(CommentDTO commentDTO){
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        return comment;
    }

    private String authanticatedUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return username;
    }
}
