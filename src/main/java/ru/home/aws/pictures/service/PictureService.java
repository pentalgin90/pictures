package ru.home.aws.pictures.service;

import ru.home.aws.pictures.dto.Picture;

public interface PictureService {
    Iterable<Picture> getPictures();
    Picture getPictureById(Long id);
    Picture getRandom();
    Picture getPictureByName(String name);
    Picture create(Picture picture);
    Picture update(Long id, Picture picture);
    void delete(Long id);
}
