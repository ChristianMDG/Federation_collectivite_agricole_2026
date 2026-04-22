package com.exam.federation.dto;

import com.exam.federation.entity.CreateCollectivityStructure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCollectivityRequest {
    private String location;
    private List<String> members;
    private Boolean federationApproval;
    private CreateCollectivityStructure structure;
}
