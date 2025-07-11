package com.db.dsg.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberOnboardingRequest {
    private String name;
    private String email;
    private String phone;
    private Long groupId;
}
