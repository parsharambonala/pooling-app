package com.org.pool.controllers;


import com.org.pool.domain.dtos.GetNotificationResponseDto;
import com.org.pool.domain.entities.Notification;
import com.org.pool.domain.mappers.NotificationMapper;
import com.org.pool.services.NotificationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping
    public ResponseEntity<List<GetNotificationResponseDto>> getNotifications(Authentication authentication){

        List<Notification> notifications = notificationService.getNotifications(authentication.getName());
        List<GetNotificationResponseDto> notificationResponseDtos = notificationMapper.toDto(notifications);

        return ResponseEntity.ok(notificationResponseDtos);

    }

}
