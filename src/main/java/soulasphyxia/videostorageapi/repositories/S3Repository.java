package soulasphyxia.videostorageapi.repositories;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.ObjectCannedAclProvider;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import soulasphyxia.videostorageapi.model.dto.MediaFileDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


@Repository
@Log4j2
public class S3Repository {

    private final AmazonS3 client;

    private String BUCKET_NAME = "bucket";


    public S3Repository(AmazonS3 client) {
        this.client = client;
    }

    public String uploadFile(File file) {

        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, file.getName(), file)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        client.putObject(request);
        file.delete();
        return String.format("File %s uploaded successfully", file.getName());

    }

    public void putObjectInBucket(File file, String bucketName){
        createBucketWithCORS(bucketName);
        PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), file)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        client.putObject(request);
        file.delete();
    }


    public List<String> bucketObjects(String bucketName){
        return client
                .listObjects(bucketName)
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .toList();
    }

    public List<String> getBuckets(){
        return client
                .listBuckets()
                .stream()
                .map(Bucket::getName)
                .toList();
    }

    public String deleteBucket(String bucketName){
        try{
            ObjectListing objectListing = client.listObjects(bucketName);
            while (true) {
                Iterator<S3ObjectSummary> objIter = objectListing.getObjectSummaries().iterator();
                while (objIter.hasNext()) {
                    client.deleteObject(bucketName, objIter.next().getKey());
                }

                if (objectListing.isTruncated()) {
                    objectListing = client.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
            client.deleteBucket(bucketName);
            return String.format("Bucket %s deleted successfully",bucketName);
        }catch (Exception e){
            e.printStackTrace();
            return String.format("Error deleting bucket %s.",bucketName);
        }
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


    private void createBucketWithCORS(String bucketName){
        client.createBucket(bucketName);
        List<CORSRule.AllowedMethods> methods = new ArrayList<>();
        methods.add(CORSRule.AllowedMethods.PUT);
        methods.add(CORSRule.AllowedMethods.POST);
        methods.add(CORSRule.AllowedMethods.DELETE);
        methods.add(CORSRule.AllowedMethods.GET);

        CORSRule rule = new CORSRule().withId("CORSRule")
                .withAllowedMethods(methods)
                .withAllowedOrigins(List.of("*"));

        List<CORSRule> rules = new ArrayList<>();
        rules.add(rule);

        BucketCrossOriginConfiguration configuration = new BucketCrossOriginConfiguration();
        configuration.setRules(rules);

        client.setBucketCrossOriginConfiguration(bucketName,configuration);
    }




}