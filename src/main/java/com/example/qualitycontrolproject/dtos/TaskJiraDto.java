package com.example.qualitycontrolproject.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) //Configurer Jackson pour ignorer les propriétés inconnues
public class TaskJiraDto {
    private String id;
    private String self;
    private String key;
    private Fields fields;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fields {
        private String summary;
        private String description;
        private Date created;
        private Date updated;
        private Date duedate;
        private Status status;
        private Priority priority;
        private IssueType issuetype;
        private List<SubTask> subtasks;
        private Project project;
        private User reporter;
        private User assignee;
        private User creator;
        private Progress progress;
        private Long timespent;
        private Long timeestimate;
        private Long aggregatetimespent;
        private Long aggregatetimeestimate;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        private String self;
        private String id;
        private String name;
        private String description;
        private String iconUrl;
        private StatusCategory statusCategory;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatusCategory {
        private String self;
        private Long id;
        private String key;
        private String colorName;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Priority {
        private String self;
        private String id;
        private String name;
        private String iconUrl;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IssueType {
        private String self;
        private String id;
        private String name;
        private String description;
        private boolean subtask;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubTask {
        private String id;
        private String key;
        private String self;
        private SubTaskFields fields;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubTaskFields {
        private String summary;
        private Status status;
        private Priority priority;
        private IssueType issuetype;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Project {
        private String self;
        private String id;
        private String key;
        private String name;
        private String projectTypeKey;
        private ProjectCategory projectCategory;
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProjectCategory {
        private String self;
        private String id;
        private String name;
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private String self;
        private String name;
        private String key;
        private String emailAddress;
        private String displayName;
        private boolean active;
        private String timeZone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Progress {
        private Long progress;
        private Long total;
    }
}