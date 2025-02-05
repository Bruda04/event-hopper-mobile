package com.ftn.eventhopper.shared.dtos.reports;

import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class CreateReportDTO {

    private String reason;
    private SimpleAccountDTO reported;
}
