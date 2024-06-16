package soulasphyxia.videostorageapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import soulasphyxia.videostorageapi.model.HashTag;
import soulasphyxia.videostorageapi.model.Post;
import soulasphyxia.videostorageapi.repositories.HashTagRepository;
import soulasphyxia.videostorageapi.repositories.PostRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final HashTagRepository repository;
    private final PostRepository postRepository;

    public Set<HashTag> getHashTags(String[] tags){
        Set<HashTag> hashTags = new HashSet<>();
        for(String tag: tags){
            if(!repository.existsByName(tag)){
                repository.save(new HashTag(tag));
            }
            hashTags.add(repository.findByName(tag));
        }
        return hashTags;
    }


    public Map<String, Integer> getTopHashTags(){
        HashMap<String, Integer> hashMap = new HashMap<>();
        List<Post> allPosts = postRepository.findAll();
        for(Post post : allPosts){
            Set<HashTag> hashTags = post.getHashTags();
            for(HashTag hashTag : hashTags){
                if(!hashMap.containsKey(hashTag.getName())){
                    hashMap.put(hashTag.getName(),0);
                }
                hashMap.put(hashTag.getName(), hashMap.get(hashTag.getName()) + 1);
            }
        }
        List<Map.Entry<String,Integer>> list = new LinkedList<>(hashMap.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue, (a,b) -> b, LinkedHashMap::new));
    }




}
