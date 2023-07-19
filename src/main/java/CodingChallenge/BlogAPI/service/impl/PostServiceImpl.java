package CodingChallenge.BlogAPI.service.impl;

import CodingChallenge.BlogAPI.domain.Category;
import CodingChallenge.BlogAPI.domain.Post;
import CodingChallenge.BlogAPI.domain.User;
import CodingChallenge.BlogAPI.dto.PostDTO;
import CodingChallenge.BlogAPI.exception.ResourceNotFoundException;
import CodingChallenge.BlogAPI.repository.CategoryRepository;
import CodingChallenge.BlogAPI.repository.PostRepository;
import CodingChallenge.BlogAPI.repository.UserRepository;
import CodingChallenge.BlogAPI.security.auth.response.PostResponse;
import CodingChallenge.BlogAPI.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = mapToEntity(postDTO);
        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(()->
                        new ResourceNotFoundException("Category",
                                "Id",
                                postDTO.getCategoryId()
                        ));
        User user = userRepository.findByEmail(authenticatedUsername()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        post.setCategory(category);
        post.setUser(user);
        Post responsePost = postRepository.save(post);
        PostDTO postResponse = mapToDTO(responsePost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sort, String sortDir) {
        Sort sortBy = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort)
                :Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sortBy);
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> listOfPosts = posts.getContent();
        List<PostDTO> listOfPostDto = listOfPosts
                .stream()
                .map(post ->
                        mapToDTO(post))
                .collect(Collectors.toList());

        PostResponse postResponse =
                new PostResponse();
        postResponse.
                setLisOfPostDto(listOfPostDto);
        postResponse.
                setPageNo(posts.getNumber());
        postResponse.
                setPageSize(posts.getSize());
        postResponse.
                setTotalElements(posts.getTotalElements());
        postResponse.
                setTotalPages(postResponse.getTotalPages());
        postResponse.
                setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDTO findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Post", "id", postId));
        PostDTO postDTO = mapToDTO(post);
        return postDTO;
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long postId) {
        Post responsePost = postRepository.findById(postId).
                orElseThrow(()->
                        new ResourceNotFoundException("Post", "id", postId));
        responsePost.
                setTitle(postDTO.getTitle());
        responsePost.
                setDescription(postDTO.getDescription());
        responsePost.
                setContent(postDTO.getContent());
        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(()->
                        new ResourceNotFoundException("Category",
                                "Id",
                                postDTO.getCategoryId()
                        ));
        responsePost.setCategory(category);
        postRepository.save(responsePost);
        PostDTO responsePostDto = mapToDTO(responsePost);

        return responsePostDto;
    }

    @Override
    public String deletePostById(Long postId) {
        postRepository.deleteById(postId);
        return "Post successfully Deleted.";
    }

    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        List<PostDTO> postDTOS = posts
                .stream()
                .map(post -> mapToDTO(post))
                .collect(Collectors.toList());
        return postDTOS;
    }

    private PostDTO mapToDTO(Post post){
        PostDTO postDTO = modelMapper.map(post,PostDTO.class);
        return postDTO;
    }

    private Post mapToEntity(PostDTO postDTO){
        Post post = modelMapper.map(postDTO, Post.class);
        return post;
    }

    private String authenticatedUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return username;
    }
}
