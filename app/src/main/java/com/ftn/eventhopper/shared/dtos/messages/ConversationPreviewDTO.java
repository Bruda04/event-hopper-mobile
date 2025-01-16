package com.ftn.eventhopper.shared.dtos.messages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ConversationPreviewDTO {
    private String username;
    private String name;
    private String surname;
    private String profilePictureUrl;
    private String lastMessage;
    private LocalDateTime lastMessageTimestamp;
}
