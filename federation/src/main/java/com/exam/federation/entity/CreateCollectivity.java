package com.exam.federation.entity;

import java.util.List;

public class CreateCollectivity {
        private String location;
        private Boolean federationApproval;
        private List<String> members;
        private CreateCollectivityStructure structure;

        public CreateCollectivity() {}

        public String getLocation() {
            return location;
        }
        public void setLocation(String location) {
            this.location = location;
        }

        public Boolean getFederationApproval() {
            return federationApproval;
        }
        public void setFederationApproval(Boolean federationApproval) {
            this.federationApproval = federationApproval;
        }

        public List<String> getMembers() {
            return members;
        }
        public void setMembers(List<String> members) {
            this.members = members;
        }

        public CreateCollectivityStructure getStructure() {
            return structure;
        }
        public void setStructure(CreateCollectivityStructure structure) {
            this.structure = structure;
        }
}
