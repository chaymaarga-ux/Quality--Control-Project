package com.example.qualitycontrolproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJiraDto {
    private String id;
    private String key;
    private String name;
    private String self;
    private String projectTypeKey;
    private LeadDto leadDto;
    private ProjectCategory projectCategory;

    @Data
    public static class ProjectCategory {
        private String id;
        private String name;
        private String description;
    }

    @Data
    public static class LeadDto {
        private String displayName;

    }
}
