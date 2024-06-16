package soulasphyxia.videostorageapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soulasphyxia.videostorageapi.model.HashTag;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    boolean existsByName(String name);
    HashTag findByName(String name);
}
