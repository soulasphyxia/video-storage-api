package soulasphyxia.videostorageapi.services;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.repositories.PostRepository;
import soulasphyxia.videostorageapi.repositories.S3Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final S3Repository s3Repository;
    private final String url;

    public PostService(PostRepository postRepository,
                       S3Repository s3Repository,
                       @Value("${url}") String url){
        this.postRepository = postRepository;
        this.s3Repository = s3Repository;
        this.url = url;

    }

    public String uploadPost(MultipartFile multipartFile, String title, String content) throws IOException {

        Date date = new Date();

        String fileName = generateFilename(multipartFile.getOriginalFilename(), date);

        File file = new File(fileName);

        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(multipartFile.getBytes());
        }

        s3Repository.uploadFile(file);

        Post post = Post.builder()
                .content(content)
                .title(title)
                .createdAt(date)
                .mediaFilePath(getFileDownloadLink(fileName))
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
            String filename = post.getMediaFilePath().replace(url +"/bucket/", "");
            System.out.println(filename);
            s3Repository.deleteFile(filename);
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

    private String getFileDownloadLink(String filename){
        System.out.println(url);
        return String.format(url + filename);
    }

    private String generateFilename(String filename, Date date){
        return date.getTime() + "-" + filename.replace(" ","_");
    }

}