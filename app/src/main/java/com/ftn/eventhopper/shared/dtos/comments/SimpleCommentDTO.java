package com.ftn.eventhopper.shared.dtos.comments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SimpleCommentDTO {
    private UUID id;
    private String content;
    private SimpleCommentAuthorDTO author;
}
