package soulasphyxia.videostorageapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import soulasphyxia.videostorageapi.model.HashTag;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    boolean existsByName(String name);
    HashTag findByName(String name);
}
