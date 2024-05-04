package soulasphyxia.videostorageapi.repositories;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.ObjectCannedAclProvider;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;


@Repository
@Log4j2
public class S3Repository {

    private final AmazonS3 client;

    private String BUCKET_NAME = "bucket";


    public S3Repository(AmazonS3 client) {
        this.client = client;
        createBucket();
    }

    public String uploadFile(File file) {

        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, file.getName(), file)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        client.putObject(request);
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

    public void createBucket(){
        client.createBucket(BUCKET_NAME);
    }

}