package ru.home.aws.pictures.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.util.Optional;

@Component
public class S3Config {
    @Value("${aws.accessKey}")
    private String key;
    @Value("${aws.secretKey}")
    private String secret;
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Bean
    public AmazonS3 getS3client(){
        AWSCredentials credentials = new BasicAWSCredentials(key, secret);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    @Bean
    public Bucket getBucket(){
        AmazonS3 s3client = getS3client();
        if (s3client.doesBucketExistV2(bucketName)) {
            Optional<Bucket> first = s3client.listBuckets()
                                                    .stream()
                                                    .filter(bucket -> bucketName.equals(bucket.getName()))
                                                    .findFirst();
            if (first.isPresent()) {
                return first.get();
            }
            return null;
        }
        return s3client.createBucket(bucketName);
    }

}
