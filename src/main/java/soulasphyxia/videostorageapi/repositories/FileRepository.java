package soulasphyxia.videostorageapi.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soulasphyxia.videostorageapi.model.PostFile;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<PostFile, Long> {
    Optional<PostFile> findByFilename(String filename);
}
