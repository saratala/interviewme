package com.interviewme.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
public class InterviewSession {

    @Id
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    private Participant participant;

    private Instant startTime;
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.startTime = Instant.now();
        this.status = InterviewStatus.CREATED;
    }

    public InterviewSession() {
        // Empty constructor required by JPA
    }
}