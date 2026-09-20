package com.org.pool.services.impl;

import com.org.pool.domain.entities.Employee;
import com.org.pool.domain.entities.Notification;
import com.org.pool.repositories.EmployeeRepository;
import com.org.pool.repositories.NotificationRepository;
import com.org.pool.services.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Notification> getNotifications(String email) {

        Employee employee = employeeRepository.findByMailId(email);

        if(employee == null) {
            throw new EntityNotFoundException("No employee found with email "+ email);
        }

        List<Notification> notifications = notificationRepository.findByPassenger(employee);

         notifications.forEach(notification -> notification.setRead(true));

         return notifications;


    }
}
