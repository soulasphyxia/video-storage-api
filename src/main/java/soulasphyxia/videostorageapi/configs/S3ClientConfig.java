package soulasphyxia.videostorageapi.configs;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3ClientConfig {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;


    public AWSCredentials getCredentials(){
        return new BasicAWSCredentials(accessKey,
                secretKey);
    }

    public AwsClientBuilder.EndpointConfiguration getEndpoint(){
            return new AwsClientBuilder.EndpointConfiguration("https://s3.storage.selcloud.ru", "ru-1");
    }


    @Bean
    public AmazonS3 initS3Client(){
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(getEndpoint())
                .build();
    }

}
