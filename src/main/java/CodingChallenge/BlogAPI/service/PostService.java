package CodingChallenge.BlogAPI.service;

import CodingChallenge.BlogAPI.dto.PostDTO;
import CodingChallenge.BlogAPI.security.auth.response.PostResponse;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);

    PostResponse getAllPosts(int pageNo, int PageSize, String sort, String sortDir);

    PostDTO findPostById(Long postId);

    PostDTO updatePost(PostDTO postDTO, Long postId);

    String deletePostById(Long postId);

    List<PostDTO> getPostsByCategory(Long categoryId);
}
