package soulasphyxia.videostorageapi.controllers;

import com.amazonaws.util.IOUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import soulasphyxia.videostorageapi.model.dto.MediaFileDTO;
import soulasphyxia.videostorageapi.services.MediaService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;


    @GetMapping("/{filename}")
    public ResponseEntity<?> getFile(@PathVariable String filename) throws IOException {
        System.out.println(filename);
        HttpHeaders headers = new HttpHeaders();
        MediaFileDTO mediaFileDTO = mediaService.getByFilename(filename);
        byte[] media = IOUtils.toByteArray(mediaFileDTO.getInputStream());
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return ResponseEntity.ok().headers(headers).contentType(MediaType.valueOf(mediaFileDTO.getContentType())).body(media);
    }


}
