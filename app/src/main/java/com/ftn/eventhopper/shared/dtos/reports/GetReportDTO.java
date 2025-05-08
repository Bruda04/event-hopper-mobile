package com.ftn.eventhopper.shared.dtos.reports;

import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class GetReportDTO {

    private UUID id;
    private String reason;
    private LocalDateTime timestamp;
    private SimpleAccountDTO reporter;
    private SimpleAccountDTO reported;
}
