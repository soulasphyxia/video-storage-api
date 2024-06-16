package soulasphyxia.videostorageapi.services;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import soulasphyxia.videostorageapi.model.HashTag;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.repositories.PostRepository;
import soulasphyxia.videostorageapi.repositories.S3Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final S3Repository s3Repository;
    private final String url;
    private final S3Service s3Service;

    public PostService(PostRepository postRepository,
                       S3Repository s3Repository,
                       S3Service s3Service,
                       @Value("${url}") String url){
        this.postRepository = postRepository;
        this.s3Repository = s3Repository;
        this.url = url;
        this.s3Service = s3Service;

    }

    public String uploadPostWithTags(MultipartFile multipartFile,
                                     String title,
                                     String content,
                                     Set<HashTag> hashTags) throws IOException, InterruptedException {

        Date date = new Date();

        String fileName = generateFilename(multipartFile.getOriginalFilename(), date);

        File file = new File(fileName);

        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(multipartFile.getBytes());
        }

        s3Repository.uploadFile(file);
        String bucketName = UUID.randomUUID().toString();

        s3Service.parseToHLS(getFileDownloadLink(fileName,bucketName));

        Post post = Post.builder()
                .content(content)
                .title(title)
                .createdAt(date)
                .hashTags(hashTags)
                .mediaFilePath(generateMediaFilePath(bucketName))
                .build();
        postRepository.save(post);

        return "Post uploaded successfully";
    }

    public String uploadPost(MultipartFile multipartFile,
                             String title,
                             String content) throws IOException, InterruptedException {
        Date date = new Date();

        String fileName = generateFilename(multipartFile.getOriginalFilename(), date);

        File file = new File(fileName);

        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(multipartFile.getBytes());
        }
        String bucketName = UUID.randomUUID().toString();
        s3Repository.putObjectInBucket(file, bucketName);
        s3Service.parseToHLS(getFileDownloadLink(fileName,bucketName));

        Post post = Post.builder()
                .content(content)
                .title(title)
                .createdAt(date)
                .mediaFilePath(generateMediaFilePath(bucketName))
                .build();
        postRepository.save(post);

        return "Post uploaded successfully";

    }

    public Page<Post> getAll(Integer page){
        return postRepository.findAll(PageRequest.of(page,10));
    }

    public Post getById(Long id){
        return postRepository.findById(id).orElse(null);
    }




    public Post deletePost(Long id){
        Post post = postRepository.findById(id).orElse(null);
        if(post != null){
            Pattern regex = Pattern.compile("(?<=8000\\/)[^\\/]+", Pattern.MULTILINE);
            Matcher matcher = regex.matcher(post.getMediaFilePath());
            String bucketName = "";
            if(matcher.find()){
                bucketName = matcher.group(0);
            }
            s3Repository.deleteBucket(bucketName);
            postRepository.deleteById(id);
            return post;
        }
        return null;
    }

    public Post patchPost(Long id, String title, String content){
        Post post = postRepository.findById(id).orElse(null);
        if(post != null){
            post.setTitle(title);
            post.setContent(content);
            postRepository.save(post);

            return post;
        }

        return null;
    }

    private String getFileDownloadLink(String filename, String bucketName){
        return String.format(url +String.format("%s/",bucketName)+ filename);
    }

    private String generateMediaFilePath(String bucketName){
        return String.format("%s%s/master.m3u8",url,bucketName);
    }


    private String generateFilename(String filename, Date date){
        return date.getTime() + "-" + filename.replace(" ","_");
    }

}