package com.exam.federation.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectivityStructureResponse {
    private MemberResponse president;
    private MemberResponse vicePresident;
    private MemberResponse treasurer;
    private MemberResponse secretary;
}
