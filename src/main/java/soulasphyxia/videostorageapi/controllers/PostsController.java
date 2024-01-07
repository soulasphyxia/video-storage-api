package soulasphyxia.videostorageapi.controllers;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.services.PostService;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin("*")
@AllArgsConstructor

public class PostsController {

    private final PostService postService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        Post post = postService.getById(id);
        if(post != null){
            return ResponseEntity.ok().body(post);
        }
        return ResponseEntity.ok().body("No post with such id");
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(name = "page", defaultValue = "0") Integer page){
        Page<Post> posts = postService.getAll(page);
        return ResponseEntity.ok().body(posts);
    }
}
