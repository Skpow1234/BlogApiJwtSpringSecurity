package CodingChallenge.BlogAPI.repository;

import CodingChallenge.BlogAPI.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findAllById(Long postId);
    List<Post> findByCategoryId(Long categoryId);
}

