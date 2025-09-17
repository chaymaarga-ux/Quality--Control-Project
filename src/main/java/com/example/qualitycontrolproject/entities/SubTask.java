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
public class SubTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subTaskId;
    private String url;
    private String subTaskKey; //(ex: "TASK-456")

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

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private Task parentTask; // Linking subtask to a task

}
