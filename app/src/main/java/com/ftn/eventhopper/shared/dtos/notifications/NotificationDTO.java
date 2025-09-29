package com.ftn.eventhopper.shared.dtos.notifications;

import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    private UUID id;
    private String content;
    private LocalDateTime timestamp;
    private SimpleProductDTO productId;
    private SimpleEventDTO eventId;
}
