package soulasphyxia.videostorageapi.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soulasphyxia.videostorageapi.repositories.S3Repository;
import soulasphyxia.videostorageapi.utils.VideoProcessor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Repository s3Repository;

    public List<String> getObjects(String bucket) {
        return s3Repository.bucketObjects(bucket);
    }


    public String parseToHLS(String url) throws IOException, InterruptedException {
        Pattern regex = Pattern.compile("(?<=8000\\/)[^\\/]+", Pattern.MULTILINE);
        Matcher matcher = regex.matcher(url);
        String bucketName = "";
        if(matcher.find()){
            bucketName = matcher.group(0);
        }
        String tempDir = VideoProcessor.mp4Script(url);
        File dir = new File(tempDir);
        List<String> files = Stream.of(dir.listFiles()).filter(file -> !file.isDirectory()).map(File::getName).toList();
        System.out.println(files.size());
        for(String file : files){
            s3Repository.putObjectInBucket(new File(tempDir+file), bucketName);
        }
        dir.delete();
        return bucketName;
    }


    public List<String> getBuckets() {
        return s3Repository.getBuckets();
    }

    public String deleteBucket(String bucket) {
        return s3Repository.deleteBucket(bucket);
    }
}
