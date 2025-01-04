package com.ftn.eventhopper.shared.dtos.events;

import android.graphics.drawable.Drawable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardEventDTO {
    String Title;
    String Secondary;
    String Description;
    Drawable Image;
}
