package soulasphyxia.videostorageapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soulasphyxia.videostorageapi.model.dto.MediaFileDTO;
import soulasphyxia.videostorageapi.repositories.S3Repository;

import java.io.File;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final S3Repository s3Repository;


    public MediaFileDTO getByFilename(String filename){
        return s3Repository.getFile(filename);
    }

}
