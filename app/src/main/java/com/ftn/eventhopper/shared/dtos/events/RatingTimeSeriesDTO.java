package com.ftn.eventhopper.shared.dtos.events;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingTimeSeriesDTO {
    private LocalDateTime timestamp;
    private double averageRating;
}
