package soulasphyxia.videostorageapi.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import soulasphyxia.videostorageapi.services.S3Service;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

//    @PostMapping("/hls")
//    public String toHls(@RequestParam String url) throws IOException, InterruptedException {
//        return s3Service.createBucket(url);
//    }


    @GetMapping("/buckets")
    public List<String> getBuckets(){
        return s3Service.getBuckets();
    }


    @GetMapping("/buckets/{bucket}")
    public List<String> getBucketObjects(@PathVariable String bucket){
        return s3Service.getObjects(bucket);
    }

    @DeleteMapping("/buckets/{bucket}")
    public String deleteBucket(@PathVariable String bucket){
        return s3Service.deleteBucket(bucket);
    }

}



