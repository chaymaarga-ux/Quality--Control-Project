package com.example.qualitycontrolproject.WebController;


import com.example.qualitycontrolproject.Services.SubTaskService;
import com.example.qualitycontrolproject.Services.TaskService;
import com.example.qualitycontrolproject.entities.SubTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/SubTaskController")
//responsible for handling incoming HTTP requests and providing appropriate responses.
@CrossOrigin(origins = "*")
public class SubTaskController {
    @Autowired
    private final SubTaskService subTaskService;
    @Autowired
    private final TaskService taskService;


    public SubTaskController(SubTaskService subTaskService, TaskService taskService) {
        this.subTaskService = subTaskService;
        this.taskService = taskService;
    }

    // Get all SubTasks
    @GetMapping
    public List<SubTask> getAllSubTasks() {
        return subTaskService.getAllSubTasks();
    }

    //  Get a SubTask by ID
    @GetMapping("/{id}")
    public Optional<SubTask> getSubTaskById(@PathVariable Long id) {
        return subTaskService.getSubTaskById(id);
    }

    // Get a SubTask by Key
    @GetMapping("/key/{subTaskKey}")
    public SubTask getSubTaskByKey(@PathVariable String subTaskKey) {
        return subTaskService.getSubTaskByKey(subTaskKey);
    }

    //  Get SubTasks by Status
    @GetMapping("/status/{status}")
    public List<SubTask> getSubTasksByStatus(@PathVariable String status) {
        return subTaskService.getSubTasksByStatus(status);
    }

    // Get SubTasks assigned to a user
    @GetMapping("/assignee/{assignee}")
    public List<SubTask> getSubTasksByAssignee(@PathVariable String assignee) {
        return subTaskService.getSubTasksByAssignee(assignee);
    }

    //  Get SubTasks by Parent Task ID
    @GetMapping("/task/{taskId}")
    public List<SubTask> getSubTasksByParentTask(@PathVariable Long taskId) {
        return subTaskService.getSubTasksByParentTask(taskId);
    }

    //  Create a new SubTask
    @PostMapping
    public SubTask createSubTask(@RequestBody SubTask subTask) {
        return subTaskService.createSubTask(subTask);
    }

    //  Delete a SubTask by ID
    @DeleteMapping("/{id}")
    public void deleteSubTask(@PathVariable Long id) {
        subTaskService.deleteSubTask(id);
    }
}
