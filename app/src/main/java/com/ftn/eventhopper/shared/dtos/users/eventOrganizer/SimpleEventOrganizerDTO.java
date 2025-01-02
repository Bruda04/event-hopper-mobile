package com.ftn.eventhopper.shared.dtos.users.eventOrganizer;

import com.ftn.eventhopper.shared.dtos.users.person.SimplePersonDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class SimpleEventOrganizerDTO extends SimplePersonDTO {
}