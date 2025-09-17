package com.example.qualitycontrolproject.WebController;


import com.example.qualitycontrolproject.Services.JiraIntegrationService;
import com.example.qualitycontrolproject.dtos.ProjectJiraDto;
import com.example.qualitycontrolproject.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jira")
@CrossOrigin("*")
public class JiraController {

    private final JiraIntegrationService jiraService;

    public JiraController(JiraIntegrationService jiraService) {
        this.jiraService = jiraService;
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectJiraDto>> getProjects() {
        return ResponseEntity.ok(jiraService.getAllProjects());

    }

    @GetMapping("/projects/{projectKey}")
    public ResponseEntity<Object> getProjectDetail(@PathVariable String projectKey) {
        return ResponseEntity.ok(jiraService.getProjectDetails(projectKey));
    }
    @GetMapping("/issues/{issueKey}")
    public ResponseEntity<Object> getIssueDetails(@PathVariable String issueKey) {
        return ResponseEntity.ok(jiraService.getIssueDetails(issueKey));
    }


    @GetMapping("/projects/{projectKey}/issues")
    public ResponseEntity<Object> getIssuesByProject(@PathVariable String projectKey) {
        return ResponseEntity.ok(jiraService.getIssuesForProject(projectKey));
    }

    @GetMapping("/projects/{projectKey}/main-tasks")
    public  ResponseEntity<Object> getMainTasks(@PathVariable String projectKey) {
        return ResponseEntity.ok(jiraService.getParentTasksForProject(projectKey));
    }


    @GetMapping("/projects/{projectKey}/subtasks")
    public  ResponseEntity<Object> getSubTasksByProject(@PathVariable String projectKey) {
        return ResponseEntity.ok(jiraService.getSubTasksForProject(projectKey));
    }

    //subtasks of a task
    @GetMapping("/tasks/{parentKey}/subtasks")
    public  ResponseEntity<Object> getSubTasks(@PathVariable String parentKey) {
        return ResponseEntity.ok(jiraService.getSubTasksOfTask(parentKey));
    }


    @GetMapping("/filter/{code}")
    public  Map<String, List<Map<String, Object>>>  getFilteredIssues(@PathVariable String code,
                                                       @RequestParam(defaultValue = "100") int maxResults) {
        try {
            return jiraService.searchIssuesWithCode(code, maxResults);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }



    @GetMapping("/grouped")
    public ResponseEntity<Map<String, Map<String, List<Map<String, Object>>>>> getGroupedResults(
            @RequestParam(defaultValue = "50") int maxResults) {
        Map<String, Map<String, List<Map<String, Object>>>> data =
                jiraService.searchAllFiltersGroupedByProject(maxResults);
        return ResponseEntity.ok(data);
    }





    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllFilteredResults(
            @RequestParam(defaultValue = "50") int maxResults) {
        List<Map<String, Object>> results = jiraService.searchAllFiltersForAllProjects(maxResults);
        return ResponseEntity.ok(results);
    }




  /*  @GetMapping("/filtered-issues")
    public List<Map<String, Object>> getFilteredIssues() {
        return jiraService.searchIssuesWithFilter();
    }*/


}