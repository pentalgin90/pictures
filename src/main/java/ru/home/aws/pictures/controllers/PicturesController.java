package ru.home.aws.pictures.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.home.aws.pictures.dto.Picture;
import ru.home.aws.pictures.service.PictureService;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("/pictures")
public class PicturesController {

    private final PictureService pictureService;
    private final AmazonS3 amazonS3;
    private final Bucket bucket;

    public PicturesController(PictureService pictureService, AmazonS3 amazonS3, Bucket bucket){
        this.pictureService = pictureService;
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    @GetMapping
    public ResponseEntity<Iterable<Picture>> getPictures(){
        return new ResponseEntity<>(pictureService.getPictures(), HttpStatus.OK);
    }

    @GetMapping("{name}")
    public ResponseEntity<Picture> getPicture(@PathVariable String name){
        return new ResponseEntity<>(pictureService.getPictureByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Picture> uploadPicture(@RequestParam("imageFile") MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket.getName(), file.getOriginalFilename(), file.getInputStream(), metadata);
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        putObjectRequest.setAccessControlList(acl);
        amazonS3.putObject(putObjectRequest);
        String url = String.format("https://s3.amazonaws.com/%s/%s", bucket.getName(), file.getOriginalFilename());
        Picture picture = new Picture(file.getOriginalFilename(), url);
        return new ResponseEntity<>(pictureService.create(picture), HttpStatus.CREATED);
    }

    @GetMapping("/random")
    public ResponseEntity<Picture> random(){
        return new ResponseEntity<>(pictureService.getRandom(), HttpStatus.OK);
    }
    
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        Picture pictureById = pictureService.getPictureById(id);
        amazonS3.deleteObject(bucket.getName(), pictureById.getName());
        pictureService.delete(id);
    }
}
