package com.org.pool.domain.mappers;

import com.org.pool.domain.dtos.GetNotificationResponseDto;
import com.org.pool.domain.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    GetNotificationResponseDto toDto(Notification notification);
    List<GetNotificationResponseDto> toDto(List<Notification> notificationList);

}
