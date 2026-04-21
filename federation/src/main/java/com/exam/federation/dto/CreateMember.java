package com.exam.federation.dto;

import com.exam.federation.entity.Enums.Gender;
import com.exam.federation.entity.Enums.MemberOccupation;
<<<<<<< HEAD
=======
import com.exam.federation.repository.MemberRepository;
>>>>>>> 186917116bb03fc3c4db4cd6a535cd955f857f7e
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMember {
<<<<<<< HEAD
=======
    private String id;
>>>>>>> 186917116bb03fc3c4db4cd6a535cd955f857f7e
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private Integer phoneNumber;
    private String email;
    private MemberOccupation occupation;
    private String collectivityIdentifier;
    private List<String> referees;
    private Boolean registrationFeePaid;
    private Boolean membershipDuesPaid;
}
