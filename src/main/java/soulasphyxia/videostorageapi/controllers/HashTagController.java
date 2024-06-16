package soulasphyxia.videostorageapi.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soulasphyxia.videostorageapi.services.HashTagService;

@RestController
@RequestMapping("/api/v1/hashtags")
@RequiredArgsConstructor
public class HashTagController {
    private final HashTagService hashTagService;
    @GetMapping
    public ResponseEntity<?> getTopHashTags(){
        return ResponseEntity.ok().body(hashTagService.getTopHashTags());
    }
}
