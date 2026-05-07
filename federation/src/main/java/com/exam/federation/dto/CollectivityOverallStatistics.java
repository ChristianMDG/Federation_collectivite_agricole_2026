package com.exam.federation.dto;

import lombok.Data;

@Data
public class CollectivityOverallStatistics {
    private CollectivityInformation collectivityInformation;
    private int newMembersNumber;
    private double overallMemberCurrentDuePercentage;
    private Double overallMemberAssiduityPercentage;
}
