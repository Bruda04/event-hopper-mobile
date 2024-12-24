package com.ftn.event_hopper.models.events;

import com.ftn.event_hopper.models.agendaActivities.AgendaActivity;
import com.ftn.event_hopper.models.eventTypes.EventType;
import com.ftn.event_hopper.models.locations.Location;
import com.ftn.event_hopper.models.shared.EventPrivacyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int maxAttendance;

    @Column
    private String description;

    @Column(nullable = false)
    private EventPrivacyType privacy;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column
    private String picture;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType eventType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private Set<AgendaActivity> agendaActivities = new HashSet<AgendaActivity>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

}
