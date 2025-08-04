package com.ftn.eventhopper.shared.dtos.events;


import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.location.SimpleLocationDTO;
import com.ftn.eventhopper.shared.dtos.messages.ConversationPreviewDTO;
import com.ftn.eventhopper.shared.models.events.EventPrivacyType;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SinglePageEventDTO {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime time;
    private String picture;
    private SimpleEventTypeDTO eventType;
    private SimpleLocationDTO location;
    private EventPrivacyType privacy;
    private boolean eventOrganizerLoggedIn;
    private boolean graphAuthorized;
    private boolean favorite;
    private ConversationPreviewDTO conversationInitialization;
}