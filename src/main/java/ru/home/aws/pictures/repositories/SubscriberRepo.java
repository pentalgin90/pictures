package ru.home.aws.pictures.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.aws.pictures.dto.Subsciber;
@Repository
public interface SubscriberRepo extends CrudRepository<Subsciber, Long> {
}
