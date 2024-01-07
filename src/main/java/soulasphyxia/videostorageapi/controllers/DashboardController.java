package soulasphyxia.videostorageapi.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import soulasphyxia.videostorageapi.services.PostService;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/dashboard")
@Log4j2
@AllArgsConstructor
@CrossOrigin("*")
public class DashboardController {

    private final PostService postService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPost(@RequestParam("data") MultipartFile file,
                                        @RequestParam("title") String title,
                                        @RequestParam("content") String content) throws IOException {
        String uploadPostResult = postService.uploadPost(file,title,content);
        return ResponseEntity.ok().body(uploadPostResult);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<?> patchPost(@PathVariable Long id,
                          @RequestParam String title,
                          @RequestParam String content){
        String actionResult = postService.patchPost(id,title,content);
        return ResponseEntity.ok().body(actionResult);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        String actionResult = postService.deletePost(id);
        return ResponseEntity.ok().body(actionResult);
    }

}
