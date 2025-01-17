package com.ftn.eventhopper.shared.dtos.messages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDTO {
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime timestamp;
    private boolean sentByMe;
}
