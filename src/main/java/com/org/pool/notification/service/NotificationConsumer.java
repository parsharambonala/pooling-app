package com.org.pool.notification.service;

import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Notification;
import com.org.pool.domain.entities.Ride;
import com.org.pool.notification.entities.RideCancellationNotificationEvent;
import com.org.pool.notification.entities.RideNotificationEvent;
import com.org.pool.repositories.EmployeeRepository;
import com.org.pool.repositories.NotificationRepository;
import com.org.pool.repositories.RideRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Component
public class NotificationConsumer {

    String topic;
    Properties properties = new Properties();
    KafkaConsumer<UUID, RideNotificationEvent> consumer;

    private final EmployeeRepository employeeRepository;
    private final RideRepository rideRepository;
    private final NotificationRepository notificationRepository;

    public NotificationConsumer(@Value("${kafka.notification.group-id}") String groupId, @Value("${kafka.notification.topic}") List<String> topics, EmployeeRepository employeeRepository,
                                RideRepository rideRepository, NotificationRepository notificationRepository) {

        this.employeeRepository = employeeRepository;
        this.rideRepository = rideRepository;
        this.notificationRepository = notificationRepository;

        properties.setProperty("bootstrap.servers", "[::1]:9092");
        properties.setProperty("group.id", groupId);
        properties.setProperty("key.deserializer", UUIDDeserializer.class.getName());
        properties.setProperty("value.deserializer", JacksonJsonDeserializer.class.getName());
        properties.setProperty("auto.offset.reset", "earliest");

        consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(topics);

    }

    @PostConstruct
    public void startConsumer() {
        new Thread(this::pollNotifications).start();
    }

    public void pollNotifications() {

        while(true) {

            ConsumerRecords<UUID, RideNotificationEvent> records =
                    consumer.poll(Duration.ofMillis(1000));

            for(ConsumerRecord<UUID, RideNotificationEvent> record : records) {
                RideNotificationEvent event = record.value();



                if(event instanceof RideCancellationNotificationEvent cancellationEvent) {

                    Ride ride = rideRepository.findById(event.getRideId()).orElseThrow( () ->
                            new EntityNotFoundException("Ride not found"));

                    String message = "The ride with id: " + ride.getId() +", driver : "+ ride.getDriver().getName()+ "has been cancelled";

                    List<Integer> employeeIds = cancellationEvent.getAffectedPassengerIds();

                    for(Integer id : employeeIds) {

                        Employee employee = employeeRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Employee not found"));


                        Notification notification = Notification.builder()
                                .passenger(employee)
                                .message(message)
                                .build();

                        notificationRepository.save(notification);

                    }


                }

            }

        }

    }

}
