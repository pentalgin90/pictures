package ru.home.aws.pictures.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.home.aws.pictures.dto.Picture;
import ru.home.aws.pictures.repositories.PictureRepo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Service
public class PictureServiceImpl implements PictureService{

    private final PictureRepo pictureRepo;
    private final AmazonS3 amazonS3;
    private final Bucket bucket;

    @Autowired
    public PictureServiceImpl(PictureRepo pictureRepo, AmazonS3 amazonS3, Bucket bucket){
        this.pictureRepo = pictureRepo;
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    @Override
    public Iterable<Picture> getPictures() {
        return pictureRepo.findAll();
    }

    @Override
    public Picture getPictureById(Long id) {
        return pictureRepo.findById(id).get();
    }

    @Override
    public Picture getPictureByName(String name){
        return pictureRepo.findPictureByName(name);
    }

    @Override
    public Picture getRandom() {
        Iterable<Picture> all = pictureRepo.findAll();
        List<Picture> pictureList = new ArrayList<>();
        Iterator<Picture> iterator = all.iterator();
        iterator.forEachRemaining(pictureList::add);
        Random rand = new Random();
        return pictureList.get(rand.nextInt(pictureList.size()));
    }

    @Override
    public Picture create(Picture picture) {
        return pictureRepo.save(picture);
    }

    @Override
    public Picture update(Long id, Picture picture) {
        return null;
    }

    @Override
    public void delete(Long id) {
        Picture pictureById = getPictureById(id);
        pictureRepo.delete(pictureById);
    }
}
