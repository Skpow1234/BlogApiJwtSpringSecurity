package CodingChallenge.BlogAPI.repository;

import CodingChallenge.BlogAPI.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
