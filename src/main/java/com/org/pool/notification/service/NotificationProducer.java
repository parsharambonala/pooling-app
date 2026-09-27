package com.org.pool.notification.service;


import com.org.pool.notification.entities.RideNotificationEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.UUID;

@Component
public class NotificationProducer {

    Properties properties = new Properties();
    KafkaProducer<UUID, RideNotificationEvent> producer;

    public NotificationProducer() {

        properties.setProperty("bootstrap.servers", "[::1]:9092");
        properties.setProperty("key.serializer", UUIDSerializer.class.getName());
        properties.setProperty("value.serializer", JacksonJsonSerializer.class.getName());

        producer = new KafkaProducer<>(properties);

    }

    public void sendNotification(String topic, RideNotificationEvent notification) {

        ProducerRecord<UUID, RideNotificationEvent> record = new ProducerRecord<>(topic, notification.getRideId(), notification);

        producer.send(record);

    }

}
