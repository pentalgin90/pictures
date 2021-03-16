package ru.home.aws.pictures.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationImpl implements PushNotification{

    private final AmazonSNS amazonSNS;
    private final String topicArn;

    public PushNotificationImpl(AmazonSNS amazonSNS, String topicArn){
        this.amazonSNS = amazonSNS;
        this.topicArn = topicArn;
    }

    @Override
    public void push(String fileName, String filePath){
        PublishRequest requestPublicsh = new PublishRequest();
        requestPublicsh.setMessage("Добавлена новая картинка " + fileName + "доступно по ссылке " + filePath);
        requestPublicsh.setTopicArn(topicArn);
        amazonSNS.publish(requestPublicsh);
    }
}
