package ru.home.aws.pictures.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Component
public class S3Config {
    @Value("${aws.accessKey}")
    private String key;
    @Value("${aws.secretKey}")
    private String secret;
    @Value("${aws.lambda.create-bucket}")
    private String urlLambda;
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Value("${aws.sns.topic.name}")
    private String topicName;

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
    public AmazonSNS getSnsClient(){
        AWSCredentials credentials = new BasicAWSCredentials(key, secret);
        return AmazonSNSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    @Bean
    public Bucket getBucket(){
        AmazonS3 s3client = getS3client();
        if (createBucket()) {
            Optional<Bucket> first = s3client.listBuckets()
                                                    .stream()
                                                    .filter(bucket -> bucketName.equals(bucket.getName()))
                                                    .findFirst();
            if (first.isPresent()) {
                return first.get();
            }
            return null;
        }
        return null;
    }

    @Bean
    public String getTopic(){
        AmazonSNS sns = getSnsClient();
        CreateTopicResult topic = sns.createTopic(topicName);
        return topic.getTopicArn();
    }

    private boolean createBucket(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String body = restTemplate.exchange(urlLambda, HttpMethod.GET, entity, String.class).getBody();
        return body.equals("CREATED");
    }

}
