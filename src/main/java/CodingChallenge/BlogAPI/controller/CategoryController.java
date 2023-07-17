package CodingChallenge.BlogAPI.controller;

import CodingChallenge.BlogAPI.dto.CategoryDTO;
import CodingChallenge.BlogAPI.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(path = "/createCategory",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.addCategory(categoryDTO), HttpStatus.CREATED);
    }

    @GetMapping("/getCategory/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @PathVariable(name = "id") Long categoryId){
        return new ResponseEntity<>(categoryService.getCategoryById(categoryId), HttpStatus.OK);
    }

    @GetMapping("/getCategories")
    public ResponseEntity<List<CategoryDTO>> getCategories(){
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable(name = "id") Long categoryId,
            @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.updateCategory(categoryId, categoryDTO), HttpStatus.OK);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable(name = "id") Long categoryId){
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
}
