package com.db.dsg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Poll {
    @Id
    @GeneratedValue
    private Long id;

    private String question;

    @ManyToOne
    private Group group;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean closed = false;
}
