package soulasphyxia.videostorageapi.repositories;

import com.amazonaws.services.s3.AmazonS3;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;


@Repository
@AllArgsConstructor
@Log4j2
public class S3Repository {

    private final AmazonS3 client;

    private final String BUCKET_NAME = "storage";

    public String uploadFile(File file) {

        client.putObject(BUCKET_NAME,file.getName(), file);

        file.delete();

        return String.format("File %s uploaded successfully", file.getName());

    }

    public String deleteFile(String filename){
        try{
            client.deleteObject(BUCKET_NAME, filename);
            return String.format("File %s deleted successfully", filename);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return String.format("Error with deleting file %s", filename);

    }

}
