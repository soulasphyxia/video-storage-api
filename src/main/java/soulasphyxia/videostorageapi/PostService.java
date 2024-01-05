package soulasphyxia.videostorageapi;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import soulasphyxia.videostorageapi.model.PostDTO;
import soulasphyxia.videostorageapi.model.PostFile;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.repositories.FileRepository;
import soulasphyxia.videostorageapi.repositories.PostRepository;

import java.io.IOException;
import java.util.Date;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final FileRepository fileRepository;

    public String uploadPost(MultipartFile file, String title, String content) throws IOException {

        PostFile videoFile = PostFile
                .builder()
                .data(file.getBytes())
                .filename(file.getOriginalFilename())
                .type(file.getContentType())
                .build();

        Post post = Post.builder()
                .content(content)
                .title(title)
                .createdAt(new Date())
                .videoFile(videoFile)
                .build();

        fileRepository.save(videoFile);
        postRepository.save(post);

        return "Post uploaded successfully";

    }

    public PostDTO getById(Long id){
        Post post = postRepository.findById(id).orElse(null);
        String fileId = post.getVideoFile().getId().toString();
        String url = String.format("http://localhost:8080/api/v1/files/%s",fileId);
        return PostDTO.builder().title(post.getTitle()).content(post.getContent()).createdAt(post.getCreatedAt()).data(url).build();
    }


    public String deletePost(){
        return "delete";
    }

    public String patchPost(){
        return "patch";
    }

}
