package soulasphyxia.videostorageapi.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import soulasphyxia.videostorageapi.model.HashTag;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.services.HashTagService;
import soulasphyxia.videostorageapi.services.PostService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/api/v1/dashboard")
@Log4j2
@AllArgsConstructor
@CrossOrigin
public class DashboardController {

    private final PostService postService;
    private final HashTagService tagService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPost(@RequestParam("data") MultipartFile file,
                                        @RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam(value = "hashtags",required = false) String[] hashTags) throws IOException, InterruptedException {
        if (file != null){
            if(hashTags != null){
                Set<HashTag> hashTagsSet = tagService.getHashTags(hashTags);
                String uploadPostResult = postService.uploadPostWithTags(file,title,content,hashTagsSet);
                return ResponseEntity.ok().body(uploadPostResult);
            }else{
                String uploadPostResult = postService.uploadPost(file,title,content);
                return ResponseEntity.ok().body(uploadPostResult);
            }
        }

        return ResponseEntity.ok().body("Something went wrong...");
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<?> patchPost(@PathVariable Long id,
                                       @RequestParam String title,
                                       @RequestParam String content){
        Post patchedPost = postService.patchPost(id,title,content);
        if(patchedPost != null){
            return ResponseEntity.ok().body(patchedPost);
        }
        return ResponseEntity.ok().body("No post with such id");
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        Post deletedPost = postService.deletePost(id);
        if(deletedPost != null){
            return ResponseEntity.ok().body(deletedPost);
        }
        return ResponseEntity.ok().body("No post with such id");
    }

}