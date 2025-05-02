package com.interviewme.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class Participant {
    @Id
    private String id;

    private String name;
    private String email;

    @Lob
    private byte[] avatar;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public Participant() {
        // Required by JPA
    }

    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
    }
}