package ru.home.aws.pictures.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BucketServiceImpl {

    private final AmazonS3 amazonS3;
    private final Bucket bucket;

    @Autowired
    public BucketServiceImpl(AmazonS3 amazonS3, Bucket bucket){
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public void save(String name, File file){
        PutObjectResult putObjectResult = amazonS3.putObject(bucket.getName(), name, file);
    }

}
