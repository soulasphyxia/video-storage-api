package soulasphyxia.videostorageapi;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import soulasphyxia.videostorageapi.model.PostFile;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.repositories.FileRepository;
import soulasphyxia.videostorageapi.repositories.PostRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final FileRepository fileRepository;

    public String uploadPost(MultipartFile file, String title, String content) throws IOException {

        PostFile postFile = PostFile
                .builder()
                .data(file.getBytes())
                .filename(file.getOriginalFilename())
                .type(file.getContentType())
                .build();

        Post post = Post.builder()
                .content(content)
                .title(title)
                .createdAt(new Date())
                .postFile(postFile)
                .data(String.format("http://localhost:8080/api/v1/files/%s",postFile.getFilename()))
                .build();

        fileRepository.save(postFile);
        postRepository.save(post);

        return "Post uploaded successfully";
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public Post getById(Long id){
        return postRepository.findById(id).orElse(null);
    }


    public String deletePost(Long id){

        postRepository.deleteById(id);

        return "Post deleted successfully";
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

}
