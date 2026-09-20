package com.org.pool.repositories;

import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByPassenger(Employee employee);

}
