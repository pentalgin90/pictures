package ru.home.aws.pictures.service;

import ru.home.aws.pictures.dto.Subsciber;

import java.util.List;

public interface SubscriberService {
    List<Subsciber> getAll();
    Subsciber create(String email);
    void delete(Subsciber subsciber);
    Subsciber getOne(Long id);
}
