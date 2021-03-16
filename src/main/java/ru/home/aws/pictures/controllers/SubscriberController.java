package ru.home.aws.pictures.controllers;

import org.springframework.web.bind.annotation.*;
import ru.home.aws.pictures.dto.Subsciber;
import ru.home.aws.pictures.service.SubscriberService;

import java.util.List;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService){
        this.subscriberService = subscriberService;
    }

    @GetMapping
    public List<Subsciber> getAll(){
        return subscriberService.getAll();
    }

    @PostMapping
    public Subsciber create(@RequestParam(name = "email", required = true) String email){
        return subscriberService.create(email);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        Subsciber one = subscriberService.getOne(id);
        subscriberService.delete(one);
    }
}
