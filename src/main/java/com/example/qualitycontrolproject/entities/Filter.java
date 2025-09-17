package com.example.qualitycontrolproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Évite les problèmes de serialisation

public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filterId;
    private String code;
    private String name;
    @Column(length = 5000)
    private String query;
    private String description;
    private String actions;

    public Long getFilterId() {
        return filterId;
    }

    public void setFilterId(Long filterId) {
        this.filterId = filterId;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    //Since a filter can apply to multiple tasks, and a task can be found by multiple filters, we need a Many-to-Many relationship.
    @ManyToMany
    @JoinTable(name = "task_filter",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks; // Filters apply to tasks



 /*   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; // propriétaire du filtre
*/
}
