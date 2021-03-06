package ru.home.aws.pictures.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.aws.pictures.dto.Picture;

@Repository
public interface PictureRepo extends CrudRepository<Picture, Long> {
    Picture findPictureByName(String name);
}
