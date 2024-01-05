package soulasphyxia.videostorageapi.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import soulasphyxia.videostorageapi.PostService;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.model.PostDTO;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/")
@Log4j2
@AllArgsConstructor
@CrossOrigin("*")
public class PostController {

    private final PostService crudService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPost(@RequestParam("data") MultipartFile file,
                                        @RequestParam("title") String title,
                                        @RequestParam("content") String content) throws IOException {
        String uploadPostResult = crudService.uploadPost(file,title,content);
        return ResponseEntity.ok().body(uploadPostResult);
    }


    @GetMapping("/{id}")

    public ResponseEntity<?> getById(@PathVariable Long id){
        PostDTO postDTO = crudService.getById(id);
        return ResponseEntity.ok().body(postDTO);
    }


    @PatchMapping("/patch")
    public Post patchPost(){
        return new Post();
    }


    @DeleteMapping("/delete")
    public Post deletePost(){
        return new Post();
    }




}
