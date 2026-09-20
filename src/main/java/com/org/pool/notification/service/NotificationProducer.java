package com.org.pool.notification.service;


import com.org.pool.notification.entities.RideNotificationEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.Properties;
import java.util.UUID;

public class NotificationProducer {

    Properties properties = new Properties();
    String topic;
    KafkaProducer<UUID, RideNotificationEvent> producer;

    public NotificationProducer(String topic) {

        this.topic = topic;

        properties.setProperty("bootstrap.servers", "[::1]:9092");
        properties.setProperty("key.serializer", UUIDSerializer.class.getName());
        properties.setProperty("value.serializer", JacksonJsonSerializer.class.getName());

        producer = new KafkaProducer<>(properties);

    }

    public void sendNotification(RideNotificationEvent notification) {

        ProducerRecord<UUID, RideNotificationEvent> record = new ProducerRecord<>(topic, notification.getRideId(), notification);

        producer.send(record);

    }

}
