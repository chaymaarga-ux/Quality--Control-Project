package com.example.qualitycontrolproject.Services;


import com.example.qualitycontrolproject.Enum.Priority;
import com.example.qualitycontrolproject.Enum.TaskStatus;
import com.example.qualitycontrolproject.Repository.ProjectRepository;
import com.example.qualitycontrolproject.Repository.SubTaskRepository;
import com.example.qualitycontrolproject.Repository.TaskRepository;
import com.example.qualitycontrolproject.dtos.TaskJiraDto;
import com.example.qualitycontrolproject.entities.Project;
import com.example.qualitycontrolproject.entities.SubTask;
import com.example.qualitycontrolproject.entities.Task;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private JiraIntegrationService jiraIntegrationService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SubTaskRepository subtaskRepository;
    @Autowired
    private SubTaskService subTaskService;




    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //  Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get task by ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    //  Get task by Key
    public Task getTaskByKey(String taskKey) {
        return taskRepository.findByTaskKey(taskKey);
    }

    //  Get tasks by Status
    public List<Task> getTasksByStatus(String statusStr) {
        TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase()); // Convertir la String en Enum
        return taskRepository.findByStatus(status);
    }

    //  Get tasks assigned to a specific user
    public List<Task> getTasksByAssignee(String assignee) {
        return taskRepository.findByAssignee(assignee);
    }

    // Get tasks belonging to a specific Project
    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectProjectId(projectId);
    }

    //  Save a new task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    //  Update an existing task
    public Optional<Task> updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(existingTask -> {

            if (updatedTask.getTaskKey() != null && !updatedTask.getTaskKey().equals(existingTask.getTaskKey())) {
                existingTask.setTaskKey(updatedTask.getTaskKey());
            }

            if (updatedTask.getType() != null && !updatedTask.getType().equals(existingTask.getType())) {
                existingTask.setType(updatedTask.getType());
            }
            if (updatedTask.getPriority() != null && !updatedTask.getPriority().equals(existingTask.getPriority())) {
                existingTask.setPriority(updatedTask.getPriority());
            }
            if (updatedTask.getDescription() != null && !updatedTask.getDescription().equals(existingTask.getDescription())) {
                existingTask.setDescription(updatedTask.getDescription());
            }
            if (updatedTask.getStatus() != null && !updatedTask.getStatus().equals(existingTask.getStatus())) {
                existingTask.setStatus(updatedTask.getStatus());
            }
            if (updatedTask.getAssignee() != null && !updatedTask.getAssignee().equals(existingTask.getAssignee())) {
                existingTask.setAssignee(updatedTask.getAssignee());
            }
            if (updatedTask.getReporterDisplayName() != null && !updatedTask.getReporterDisplayName().equals(existingTask.getReporterDisplayName())) {
                existingTask.setReporterDisplayName(updatedTask.getReporterDisplayName());
            }
            if (updatedTask.getStartDate() != null) {
                existingTask.setStartDate(updatedTask.getStartDate());
            }
            if (updatedTask.getDueDate() != null) {
                existingTask.setDueDate(updatedTask.getDueDate());
            }
            if (updatedTask.getResolvedDate() != null) {
                existingTask.setResolvedDate(updatedTask.getResolvedDate());
            }
            if (updatedTask.getInternalEstimate() != 0) {
                existingTask.setInternalEstimate(updatedTask.getInternalEstimate());
            }
            if (updatedTask.getSalesEstimate() != 0) {
                existingTask.setSalesEstimate(updatedTask.getSalesEstimate());
            }
            if (updatedTask.getEstimated() != 0) {
                existingTask.setEstimated(updatedTask.getEstimated());
            }
            if (updatedTask.getETC() != 0) {
                existingTask.setETC(updatedTask.getETC());
            }
            if (updatedTask.getRemaining() != 0) {
                existingTask.setRemaining(updatedTask.getRemaining());
            }
            if (updatedTask.getLoggedHours() != 0) {
                existingTask.setLoggedHours(updatedTask.getLoggedHours());
            }
            if (updatedTask.getChecklistInput() != null) {
                existingTask.setChecklistInput(updatedTask.getChecklistInput());
            }
            if (updatedTask.getChecklistOutput() != null) {
                existingTask.setChecklistOutput(updatedTask.getChecklistOutput());
            }
            if (updatedTask.getDoubts() != null) {
                existingTask.setDoubts(updatedTask.getDoubts());
            }

            return taskRepository.save(existingTask);
        });
    }




    private void updateExistingTask(Task existingTask, Task newTask) {
        existingTask.setSummary(newTask.getSummary());
        existingTask.setDescription(newTask.getDescription());
        existingTask.setStatus(newTask.getStatus());
        existingTask.setPriority(newTask.getPriority());
        existingTask.setDueDate(newTask.getDueDate());
        existingTask.setUpdatedDate(newTask.getUpdatedDate());
        existingTask.setAssignee(newTask.getAssignee());
        existingTask.setAssigneeEmail(newTask.getAssigneeEmail());
        existingTask.setAssigneeDisplayName(newTask.getAssigneeDisplayName());
        existingTask.setEstimated(newTask.getEstimated());
        existingTask.setLoggedHours(newTask.getLoggedHours());
        existingTask.setRemaining(newTask.getRemaining());

        taskRepository.save(existingTask);
    }




    public int fetchAndInsertTasks() {
        System.out.println("Starting task synchronization with Jira for all projects...");
        int totalNewTasks = 0;
        int totalNewSubTasks = 0;
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();


        try {
            // Récupérer tous les projets de la base de données
            List<Project> projects = projectRepository.findAll();

            if (projects.isEmpty()) {
                System.out.println("Aucun projet trouvé dans la base de données. Veuillez d'abord synchroniser les projets.");
                return 0;
            }

            // Pour chaque projet, récupérer et insérer les tâches
            for (Project project : projects) {
                String projectKey = project.getProjectKey();
                System.out.println("Synchronisation des tâches pour le projet: " + projectKey);
                int projectNewTasks = 0;
                int projectNewSubTasks = 0;

                try {
                    // Récupérer les tâches depuis Jira
                    Object jiraIssuesObj = jiraIntegrationService.getParentTasksForProject(projectKey);

                    if (jiraIssuesObj instanceof java.util.Map) {
                        java.util.Map<String, Object> responseMap = (java.util.Map<String, Object>) jiraIssuesObj;

                        if (responseMap.containsKey("issues") && responseMap.get("issues") instanceof List) {
                            List<Object> issues = (List<Object>) responseMap.get("issues");

                            for (Object issueObj : issues) {
                                if (issueObj instanceof java.util.Map) {
                                    // Convertir l'objet Map en TaskJiraDto
                                    TaskJiraDto taskDto = mapper.convertValue(issueObj, TaskJiraDto.class);

                                    // Convertir en entité Task avec votre méthode existante
                                    Task task = convertToEntity(taskDto);
                                    task.setProject(project);

                                    // Vérifier si la tâche existe déjà
                                    Task existingTask = getTaskByKey(task.getTaskKey());
                                    Task savedTask; // Pour référencer la tâche sauvegardée

                                    if (existingTask == null) {
                                        // Sauvegarder la nouvelle tâche
                                        savedTask = insertTask(task);
                                        projectNewTasks++;
                                    } else {
                                        // Mettre à jour la tâche existante
                                        updateExistingTask(existingTask, task);
                                        savedTask = existingTask;
                                    }


                                    // NOUVELLE PARTIE: Traiter les sous-tâches
                                    if (taskDto.getFields() != null && taskDto.getFields().getSubtasks() != null
                                            && !taskDto.getFields().getSubtasks().isEmpty()) {

                                        List<TaskJiraDto.SubTask> subTasksBasicInfo = taskDto.getFields().getSubtasks();
                                        System.out.println("Traitement de " + subTasksBasicInfo.size() + " sous-tâches pour " + taskDto.getKey());

                                        int localSubTaskCount = 0; // Compteur local pour cette tâche

                                        for (TaskJiraDto.SubTask subTaskBasic : subTasksBasicInfo) {
                                            try {
                                                // Vérifier si la clé n'est pas null
                                                if (subTaskBasic.getKey() == null) {
                                                    System.err.println("Sous-tâche avec clé null détectée, ignorée");
                                                    continue;
                                                }

                                                System.out.println("Traitement de la sous-tâche: " + subTaskBasic.getKey());

                                                // Récupérer les détails complets de la sous-tâche
                                                Object subTaskDetailsObj = jiraIntegrationService.getIssueDetails(subTaskBasic.getKey());
                                                SubTask subTask;

                                                if (subTaskDetailsObj instanceof java.util.Map) {
                                                    System.out.println("Détails complets obtenus pour: " + subTaskBasic.getKey());
                                                    // Convertir l'objet Map en TaskJiraDto complet
                                                    TaskJiraDto subTaskFullDto = mapper.convertValue(subTaskDetailsObj, TaskJiraDto.class);

                                                    subTask = subTaskService.convertToEntity(subTaskFullDto, savedTask);
                                                } else {
                                                    System.out.println("Utilisation des infos basiques pour: " + subTaskBasic.getKey());
                                                    // Fallback en cas d'erreur: utiliser les infos basiques
                                                    subTask = subTaskService.convertToEntity(subTaskBasic, savedTask);
                                                }

                                                // Vérifier si la sous-tâche existe déjà
                                                SubTask existingSubTask = subTaskService.getSubTaskByKey(subTask.getSubTaskKey());
                                                if (existingSubTask == null) {
                                                    // Sauvegarder la nouvelle sous-tâche
                                                    subTaskService.createSubTask(subTask);
                                                    localSubTaskCount++;
                                                    System.out.println("Sous-tâche créée: " + subTask.getSubTaskKey());
                                                } else {
                                                    System.out.println("Sous-tâche existante ignorée: " + subTask.getSubTaskKey());
                                                }
                                            } catch (Exception e) {
                                                System.err.println("Erreur lors du traitement de la sous-tâche " + subTaskBasic.getKey() + ": " + e.getMessage());
                                                e.printStackTrace();
                                            }
                                        }

                                        // Ajouter le compteur local au compteur global
                                        projectNewSubTasks += localSubTaskCount;
                                        System.out.println("Total des sous-tâches créées pour cette tâche: " + localSubTaskCount);
                                    }
                                }
                            }
                        }
                    }

                    totalNewTasks += projectNewTasks;
                    totalNewSubTasks += projectNewSubTasks;
                    System.out.println("Terminé pour " + projectKey + ": " + projectNewTasks +
                            " nouvelles tâches et " + projectNewSubTasks + " sous-tâches ajoutées");
                } catch (Exception e) {
                    System.err.println("Erreur lors de la synchronisation des tâches pour le projet " + projectKey + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("Synchronisation complète: " + totalNewTasks + " nouvelles tâches et " +
                    totalNewSubTasks + " sous-tâches ajoutées au total");

        } catch (Exception e) {
            System.err.println("Erreur durant la synchronisation des tâches: " + e.getMessage());
            e.printStackTrace();
        }

        return totalNewTasks;
    }
    /* Convertit un DTO Jira en entité Task
     */
    private Task convertToEntity(TaskJiraDto jiraDto) {
        Task task = new Task();

        try {
            if (jiraDto != null && jiraDto.getFields() != null) {
                TaskJiraDto.Fields fields = jiraDto.getFields();

                task.setTaskKey(jiraDto.getKey());
                task.setUrl(jiraDto.getSelf());

                if (fields.getSummary() != null) {
                    task.setSummary(fields.getSummary());
                }

                if (fields.getDescription() != null) {
                    String description = fields.getDescription();

                    System.out.println("TASK ERROR DETAILS:");
                    System.out.println("Summary: " + fields.getSummary());
                    System.out.println("Description length: " + description.length());
                    System.out.println("Description début: " + description.substring(0, Math.min(description.length(), 100)) + "...");
                    System.out.println("Description fin: " + description.substring(Math.max(0, description.length() - 100)) + "...");

                    // Tronquer
                    if (description.length() > 255) {
                        description = description.substring(0, 250);
                        System.out.println("Description tronquée à 250 caractères");
                    }

                    task.setDescription(description);

                }

                // Convertir statut
                if (fields.getStatus() != null && fields.getStatus().getName() != null) {
                    task.setStatus(mapStatusFromJira(fields.getStatus().getName()));
                }
                // Convertir priorité
                if (fields.getPriority() != null && fields.getPriority().getName() != null) {
                    task.setPriority(mapPriorityFromJira(fields.getPriority().getName()));
                }

                // Dates
                task.setCreatedDate(fields.getCreated());
                task.setUpdatedDate(fields.getUpdated());
                task.setDueDate(fields.getDuedate());

                // Type
                if (fields.getIssuetype() != null) {
                    task.setIssueTypeName(fields.getIssuetype().getName());
                    task.setIssueTypeSubTask(fields.getIssuetype().isSubtask());
                    task.setType(fields.getIssuetype().getName());
                }

                // Assignee
                if (fields.getAssignee() != null) {
                    task.setAssignee(fields.getAssignee().getName());
                    task.setAssigneeEmail(fields.getAssignee().getEmailAddress());
                    task.setAssigneeDisplayName(fields.getAssignee().getDisplayName());
                }

                // Reporter
                if (fields.getReporter() != null) {
                    task.setReporterName(fields.getReporter().getName());
                    task.setReporterEmail(fields.getReporter().getEmailAddress());
                    task.setReporterDisplayName(fields.getReporter().getDisplayName());
                }

                // Creator
                if (fields.getCreator() != null) {
                    task.setCreatorEmail(fields.getCreator().getEmailAddress());
                    task.setCreatorDisplayName(fields.getCreator().getDisplayName());
                }

                // Estimation et temps
                if (fields.getTimeestimate() != null) {
                    task.setEstimated(fields.getTimeestimate() / 3600f); // Conversion en heures
                }

                // Temps passé directement sur cette tâche
                if (fields.getTimespent() != null) {
                    System.out.println("time spentt en secondes: " + fields.getTimespent()); //jai fait le test de les afficher mais tous null du coup ne saffiche passss
                    task.setLoggedHours(fields.getTimespent());
                }

                // Temps total passé incluant les sous-tâches
                if (fields.getAggregatetimespent() != null) {
                    System.out.println("Aggregate time spent en secondes: " + fields.getAggregatetimespent());
                    task.setAggregateLoggedHours(fields.getAggregatetimespent());

                    /*float hours = fields.getAggregatetimespent() / 3600f;
                    System.out.println("Aggregate time spent en heures: " + hours);
                    task.setAggregateLoggedHours(hours);*/
                }

                if (fields.getTimeestimate() != null && fields.getTimespent() != null) {
                    task.setRemaining(Math.max(0, (fields.getTimeestimate() - fields.getTimespent()) / 3600f));
                }
                // Sous-tâches (devront être ajoutées séparément)
                task.setSubTasks(new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion de la tâche: " + e.getMessage());
            e.printStackTrace();
        }

        return task;
    }




    /**
     * Mappe le statut de Jira vers l'énumération TaskStatus
     */
    private TaskStatus mapStatusFromJira(String jiraStatus) {
        if (jiraStatus == null) return TaskStatus.OPEN;

        switch (jiraStatus.toLowerCase()) {
            case "to do":
            case "open":
                return TaskStatus.OPEN;
            case "in progress":
            case "running":
                return TaskStatus.IN_PROGRESS;
            case "done":
            case "resolved":
            case "closed":
                return TaskStatus.DONE;
            case "blocked":
                return TaskStatus.BLOCKED;
            default:
                return TaskStatus.OPEN;
        }
    }
    /**
     * Mappe la priorité de Jira vers l'énumération Priority
     */
    private Priority mapPriorityFromJira(String jiraPriority) {
        if (jiraPriority == null) return Priority.MEDIUM;

        switch (jiraPriority.toLowerCase()) {
            case "highest":
            case "critical":
                return Priority.Critical;
            case "high":
                return Priority.HIGH;
            case "medium":
            case "normal":
                return Priority.MEDIUM;
            case "low":
                return Priority.LOW;
            case "lowest":
            case "minor":
                return Priority.VERY_LOW;
            default:
                return Priority.MEDIUM;
        }
    }

    //insérer une tâche
    @Transactional
    public Task insertTask(Task newTask) {
        return taskRepository.save(newTask);
    }




    // Delete a task
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
