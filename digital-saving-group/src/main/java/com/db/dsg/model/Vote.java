package com.db.dsg.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Poll poll;

    @ManyToOne
    private Member member;

    @Enumerated(EnumType.STRING)
    private VoteOption choice;

    private LocalDateTime votedAt = LocalDateTime.now();
}
