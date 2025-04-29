package com.ftn.eventhopper.shared.dtos.blocks;

import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBlockDTO {
    private UUID id;
    private LocalDateTime timestamp;
    private SimpleAccountDTO who;
    private SimpleAccountDTO blocked;
}