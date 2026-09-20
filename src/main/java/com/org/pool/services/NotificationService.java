package com.org.pool.services;

import com.org.pool.domain.entities.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotifications(String email);
}
