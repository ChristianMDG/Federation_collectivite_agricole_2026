package com.exam.federation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectivityResponse {
        private String id;
        private String number;
        private String name;
        private String location;
        private CollectivityStructureResponse structure;
        private List<MemberResponse> members;

}
