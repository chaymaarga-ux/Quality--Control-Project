package com.example.qualitycontrolproject.WebController;


import com.example.qualitycontrolproject.Services.ProjectService;
import com.example.qualitycontrolproject.entities.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ProjectController")
//responsible for handling incoming HTTP requests and providing appropriate responses.
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Get all projects
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    //  Get a project by ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get a project by key
    @GetMapping("/key/{projectKey}")
    public ResponseEntity<Project> getProjectByKey(@PathVariable String projectKey) {
        Project project = projectService.getProjectByKey(projectKey);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }

    // Get a project by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Project> getProjectByName(@PathVariable String name) {
        Project project = projectService.getProjectByName(name);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }

    //  Create a new project
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    //  Update a project
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        Optional<Project> result = projectService.updateProject(id, updatedProject);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a project by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

   /* @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(@RequestParam String term) {
        List<Project> projects = projectService.searchProjectsByName(term);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/advanced-search")
    public ResponseEntity<List<Project>> advancedSearch(@RequestParam String term) {
        List<Project> projects = projectService.searchProjects(term);
        return ResponseEntity.ok(projects);
    }*/

    @PostMapping("/sync")
    public ResponseEntity<Integer> syncProjectsFromJira() {
        int newProjects = projectService.fetchAndInsertProjects();
        return ResponseEntity.ok(newProjects);
    }
}
