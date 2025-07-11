package com.db.dsg.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String role; // PRESIDENT, TREASURER, etc.
    private String aadhaar;
    private boolean ekycVerified;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberUser user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }
}
