package com.ftn.eventhopper.shared.dtos.events;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphDataDTO {
    private List<RatingTimeSeriesDTO> ratings;
    private int maxAttendance;
    private int numPendingInvitations;
    private int numDeclinesInvitations;
    private int numAcceptedInvitations;
}

