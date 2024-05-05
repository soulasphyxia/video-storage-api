package soulasphyxia.videostorageapi.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soulasphyxia.videostorageapi.model.dto.MediaFileDTO;
import soulasphyxia.videostorageapi.services.MediaService;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;


    @GetMapping("/{filename}")
    public ResponseEntity<?> getFile(@RequestParam String filename){
        MediaFileDTO mediaFileDTO = mediaService.getByFilename(filename);
        return ResponseEntity.ok().contentType(MediaType.valueOf(mediaFileDTO.getContentType())).body(mediaFileDTO.getInputStream());
    }


}
