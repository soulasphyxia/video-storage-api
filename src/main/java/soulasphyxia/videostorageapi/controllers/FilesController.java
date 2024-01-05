package soulasphyxia.videostorageapi.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.model.PostFile;
import soulasphyxia.videostorageapi.repositories.FileRepository;
import soulasphyxia.videostorageapi.repositories.PostRepository;

import java.util.List;

@Controller
@RequestMapping("/api/v1/files")
@Log4j2
@CrossOrigin("*")
@AllArgsConstructor
public class FilesController {

    private final FileRepository fileRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        PostFile file = fileRepository.findById(id).orElse(null);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(file.getType()))
                .body(file.getData());
    }


}
