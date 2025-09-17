package com.example.qualitycontrolproject.WebController;


import com.example.qualitycontrolproject.Services.TaskService;
import com.example.qualitycontrolproject.entities.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/TaskController")
//responsible for handling incoming HTTP requests and providing appropriate responses.
@CrossOrigin(origins = "*")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //  Get all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Get a task by ID
    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    //  Get a task by key
    @GetMapping("/key/{taskKey}")
    public Task getTaskByKey(@PathVariable String taskKey) {
        return taskService.getTaskByKey(taskKey);
    }

    // Get tasks by status
    @GetMapping("/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable String status) {
        return taskService.getTasksByStatus(status);
    }

    //  Get tasks assigned to a user
    @GetMapping("/assignee/{assignee}")
    public List<Task> getTasksByAssignee(@PathVariable String assignee) {
        return taskService.getTasksByAssignee(assignee);
    }

    // Get tasks by project ID
    @GetMapping("/project/{projectId}")
    public List<Task> getTasksByProject(@PathVariable Long projectId) {
        return taskService.getTasksByProject(projectId);
    }

    //  Create a new task
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    // Update a task
   // @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Optional<Task> result = taskService.updateTask(id, updatedTask);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a task by ID
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PostMapping("/sync")
    public ResponseEntity<Integer> syncTasksFromJira() {
        int newProjects = taskService.fetchAndInsertTasks();
        return ResponseEntity.ok(newProjects);
    }

}
