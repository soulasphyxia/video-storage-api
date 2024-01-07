package soulasphyxia.videostorageapi.services;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.repositories.PostRepository;
import soulasphyxia.videostorageapi.repositories.S3Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Repository s3Repository;

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




    public String deletePost(Long id){
        Post post = postRepository.findById(id).orElse(null);
        if(post != null){
            String filename = post.getMediaFilePath().replace("https://45b92c47-7474-4880-80e3-ef8b5ab159e3.selstorage.ru/", "");
            s3Repository.deleteFile(filename);
            postRepository.deleteById(id);
            return "Post deleted successfully";
        }
        return "Error with deleting post";
    }

    public String patchPost(Long id, String title, String content){
        Post post = postRepository.findById(id).orElse(null);
        if(post != null){
            post.setTitle(title);
            post.setContent(content);
            postRepository.save(post);

            return "Post patched successfully";
        }

        return "Error";
    }

    private String getFileDownloadLink(String filename){
        return String.format("https://45b92c47-7474-4880-80e3-ef8b5ab159e3.selstorage.ru/%s", filename);
    }

    private String generateFilename(String filename, Date date){
        return date.getTime() + "-" + filename.replace(" ","_");
    }

}
