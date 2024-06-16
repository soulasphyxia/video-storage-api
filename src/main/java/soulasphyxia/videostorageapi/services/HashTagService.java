package soulasphyxia.videostorageapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import soulasphyxia.videostorageapi.model.HashTag;
import soulasphyxia.videostorageapi.repositories.HashTagRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final HashTagRepository repository;


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



}
