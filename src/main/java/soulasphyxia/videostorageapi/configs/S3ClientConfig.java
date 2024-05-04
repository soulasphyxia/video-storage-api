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

    @Value("${scality.access.key.id}")
    private String accessKey;

    @Value("${scality.secret.access.key}")
    private String secretKey;

    @Value("${s3.url}")
    private String s3Url;

    @Value("${spring.datasource.url}")
    private String url;


    public AWSCredentials getCredentials(){
        return new BasicAWSCredentials(accessKey,
                secretKey);
    }

    public AwsClientBuilder.EndpointConfiguration getEndpoint(){
        System.out.println(s3Url);
        System.out.println(url);
        return new AwsClientBuilder.EndpointConfiguration(s3Url, "us-west-2");
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