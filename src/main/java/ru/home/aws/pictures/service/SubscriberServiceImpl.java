package ru.home.aws.pictures.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.home.aws.pictures.dto.Subsciber;
import ru.home.aws.pictures.repositories.SubscriberRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubscriberServiceImpl implements SubscriberService{

    private final SubscriberRepo subscriberRepo;
    private final AmazonSNS amazonSNS;
    private final String topicARN;

    public SubscriberServiceImpl(SubscriberRepo subscriberRepo, AmazonSNS amazonSNS, String topicARN){
        this.subscriberRepo = subscriberRepo;
        this.amazonSNS = amazonSNS;
        this.topicARN = topicARN;
    }

    @Override
    public List<Subsciber> getAll() {
        Iterable<Subsciber> all = subscriberRepo.findAll();
        List<Subsciber> list = new ArrayList<>();
        all.forEach(list::add);
        return list.stream().filter(subsciber -> subsciber.getConfirmed()).collect(Collectors.toList());
    }

    @Override
    public Subsciber create(String email) {
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setTopicArn(topicARN);
        subscribeRequest.setEndpoint(email);
        subscribeRequest.setProtocol("email");
        SubscribeResult subscribe = amazonSNS.subscribe(subscribeRequest);
        String subscriptionArn = subscribe.getSubscriptionArn();
        Subsciber subsciber = new Subsciber(email, subscriptionArn);
        return subscriberRepo.save(subsciber);
    }

    @Override
    public void delete(Subsciber subsciber) {
        UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
        unsubscribeRequest.setSubscriptionArn(subsciber.getSubsciberArn());
        amazonSNS.unsubscribe(unsubscribeRequest);
        subscriberRepo.delete(subsciber);
    }

    @Override
    public Subsciber getOne(Long id){
        Optional<Subsciber> byId = subscriberRepo.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void updateSubscribeArn(){
        Iterable<Subsciber> subscribers = subscriberRepo.findAll();
        List<Subsciber> subscibers = new ArrayList<>();
        for (Subsciber s : subscribers) {
            subscibers.add(s);
        }
        Map<String, Subsciber> mapSub = new HashMap<>();
        subscibers.stream().forEach(subsciber -> {
            mapSub.put(subsciber.getEmail(), subsciber);
        });
        ListSubscriptionsByTopicResult listSubscriptionsByTopicResult = amazonSNS.listSubscriptionsByTopic(topicARN);
        List<Subscription> subscriptions = listSubscriptionsByTopicResult.getSubscriptions();
        subscriptions.stream().forEach(subscription -> {
            String endpoint = subscription.getEndpoint();
            Subsciber subsciber = mapSub.get(endpoint);
            subsciber.setSubsciberArn(subscription.getSubscriptionArn());
            mapSub.put(endpoint, subsciber);
        });
        Collection<Subsciber> values = mapSub.values();
        values.stream().forEach(subsciber -> {
            Optional<Subsciber> byId = subscriberRepo.findById(subsciber.getId());
            if (byId.isPresent()) {
                Subsciber subsciber1 = byId.get();
                subsciber1.setSubsciberArn(subsciber.getSubsciberArn());
                subsciber1.setConfirmed(true);
                subscriberRepo.save(subsciber1);
            }
        });
    }
}
