package soulasphyxia.videostorageapi.model.dto;


import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaFileDTO {

    private String filename;
    private String contentType;
    private S3ObjectInputStream inputStream;

}
