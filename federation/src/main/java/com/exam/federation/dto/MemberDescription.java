package com.exam.federation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDescription {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String occupation;
}