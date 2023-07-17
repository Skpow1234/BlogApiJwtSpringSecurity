package CodingChallenge.BlogAPI.security.auth.response;

import CodingChallenge.BlogAPI.dto.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private List<PostDTO> lisOfPostDto;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
