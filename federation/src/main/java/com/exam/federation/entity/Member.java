package com.exam.federation.entity;

import com.exam.federation.entity.Enum.Gender;

import java.time.LocalDate;
import java.util.UUID;

public class Member {
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private MemberOccupation occupation;
}
