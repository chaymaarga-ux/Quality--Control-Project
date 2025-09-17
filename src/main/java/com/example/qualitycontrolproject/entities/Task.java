package com.example.qualitycontrolproject.entities;

import com.example.qualitycontrolproject.Enum.Priority;
import com.example.qualitycontrolproject.Enum.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private String url;
    private String taskKey; //(ex: "TASK-456")
    private String type;
    private String summary;
    private Priority priority;
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    private TaskStatus status;
    private String issueTypeName;
    private Boolean issueTypeSubTask;
    private String assignee;
    private String reporterName;
    private String reporterEmail;
    private String reporterDisplayName;
    private String assigneeEmail;
    private String assigneeDisplayName;
    private String creatorEmail;
    private String creatorDisplayName;
    private Date createdDate;
    private Date updatedDate;
    private Date startDate;
    private Date dueDate;
    private Date resolvedDate;
    private float internalEstimate;
    private float salesEstimate;
    private float estimated;
    private float ETC; // Estimate to Complete
    private float remaining;
    private float loggedHours;
    private float aggregateLoggedHours;


    @ElementCollection
    private List<String> checklistInput;
    @ElementCollection
    private List<String> checklistOutput;
    @ElementCollection
    private List<String> doubts;


    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTask> subTasks;


    @ManyToMany(mappedBy = "tasks")
    private List<Filter> filters;

}
