package com.example.qualitycontrolproject.Services;


import com.example.qualitycontrolproject.Enum.Priority;
import com.example.qualitycontrolproject.Enum.TaskStatus;
import com.example.qualitycontrolproject.Repository.SubTaskRepository;
import com.example.qualitycontrolproject.dtos.TaskJiraDto;
import com.example.qualitycontrolproject.entities.SubTask;
import com.example.qualitycontrolproject.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubTaskService {
    @Autowired
    private SubTaskRepository subTaskRepository;
    @Autowired
    private SubTaskService subTaskService;



    //Get all SubTasks
    public List<SubTask> getAllSubTasks() {
        return subTaskRepository.findAll();
    }

    //Get SubTask by ID
    public Optional<SubTask> getSubTaskById(Long id) {
        return subTaskRepository.findById(id);
    }

    //Get SubTask by Key
    public SubTask getSubTaskByKey(String subTaskKey) {
        return subTaskRepository.findBySubTaskKey(subTaskKey);
    }

    //Get SubTasks by Status
    public List<SubTask> getSubTasksByStatus(String statusStr) {
        TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase()); // Convertir la String en Enum
        return subTaskRepository.findByStatus(status);
    }

    //Get SubTasks assigned to a specific user
    public List<SubTask> getSubTasksByAssignee(String assignee) {
        return subTaskRepository.findByAssignee(assignee);
    }

    //Get SubTasks belonging to a specific Task
    public List<SubTask> getSubTasksByParentTask(Long taskId) {
        return subTaskRepository.findByParentTaskTaskId(taskId);
    }

    //Save a new SubTask
    public SubTask createSubTask(SubTask subTask) {
        return subTaskRepository.save(subTask);
    }

    //Delete a SubTask
    public void deleteSubTask(Long id) {
        subTaskRepository.deleteById(id);
    }


    //  Update an existing SubTask
  /*  public Optional<SubTask> updateSubTask(Long id, SubTask updatedSubTask) {
        return subTaskRepository.findById(id).map(existingSubTask -> {
            existingSubTask.setSubTaskKey(updatedSubTask.getSubTaskKey());
            existingSubTask.setName(updatedSubTask.getName());
            existingSubTask.setPriority(updatedSubTask.getPriority());
            existingSubTask.setDescription(updatedSubTask.getDescription());
            existingSubTask.setStatus(updatedSubTask.getStatus());
            existingSubTask.setAssignee(updatedSubTask.getAssignee());
            existingSubTask.setStartDate(updatedSubTask.getStartDate());
            existingSubTask.setDueDate(updatedSubTask.getDueDate());
            existingSubTask.setEstimated(updatedSubTask.getEstimated());
            existingSubTask.setETC(updatedSubTask.getETC());
            existingSubTask.setRemaining(updatedSubTask.getRemaining());
            existingSubTask.setLoggedHours(updatedSubTask.getLoggedHours());
            existingSubTask.setChecklistInput(updatedSubTask.getChecklistInput());
            existingSubTask.setChecklistOutput(updatedSubTask.getChecklistOutput());

            return subTaskRepository.save(existingSubTask);
        });
    }*/





    /**
     * Convertit les données Jira en entité SubTask
     * Peut traiter soit un TaskJiraDto complet, soit un TaskJiraDto.SubTask
     */
    public SubTask convertToEntity(Object source, Task parentTask) {
        SubTask subTask = new SubTask();

        try {
            // Si c'est un DTO complet (résultat de getIssueDetails)
            if (source instanceof TaskJiraDto) {
                TaskJiraDto jiraDto = (TaskJiraDto) source;

                if (jiraDto != null && jiraDto.getFields() != null) {
                    TaskJiraDto.Fields fields = jiraDto.getFields();

                    // Informations de base
                    subTask.setSubTaskKey(jiraDto.getKey());
                    subTask.setUrl(jiraDto.getSelf());
                    subTask.setParentTask(parentTask);

                    // Summary et Description
                    if (fields.getSummary() != null) {
                        subTask.setSummary(fields.getSummary());
                    }

                    if (fields.getDescription() != null) {
                        String description = fields.getDescription();

                        // Debug comme dans la méthode task
                        System.out.println("SUBTASK ERROR DETAILS:");
                        System.out.println("Summary: " + fields.getSummary());
                        System.out.println("Description length: " + description.length());
                        if (description.length() > 0) {
                            System.out.println("Description début: " + description.substring(0, Math.min(description.length(), 100)) + "...");
                            System.out.println("Description fin: " + description.substring(Math.max(0, description.length() - 100)) + "...");
                        }

                        // Tronquer
                        if (description.length() > 255) {
                            description = description.substring(0, 250);
                            System.out.println("Description tronquée à 250 caractères");
                        }

                        subTask.setDescription(description);
                    }

                    // Convertir statut
                    if (fields.getStatus() != null && fields.getStatus().getName() != null) {
                        subTask.setStatus(mapStatusFromJira(fields.getStatus().getName()));
                    }
                    // Convertir priorité
                    if (fields.getPriority() != null && fields.getPriority().getName() != null) {
                        subTask.setPriority(mapPriorityFromJira(fields.getPriority().getName()));
                    }


                    // Dates
                    subTask.setCreatedDate(fields.getCreated());
                    subTask.setUpdatedDate(fields.getUpdated());
                    subTask.setDueDate(fields.getDuedate());


                    // Type
                    if (fields.getIssuetype() != null) {
                        subTask.setIssueTypeName(fields.getIssuetype().getName());
                        subTask.setIssueTypeSubTask(fields.getIssuetype().isSubtask());
                        // Si vous avez un champ type dans SubTask
                        if (subTask.getClass().getDeclaredField("type") != null) {
                            subTask.setType(fields.getIssuetype().getName());
                        }
                    }


                    // Assignee
                    if (fields.getAssignee() != null) {
                        subTask.setAssignee(fields.getAssignee().getName());
                        subTask.setAssigneeEmail(fields.getAssignee().getEmailAddress());
                        subTask.setAssigneeDisplayName(fields.getAssignee().getDisplayName());
                    }

                    // Reporter
                    if (fields.getReporter() != null) {
                        subTask.setReporterName(fields.getReporter().getName());
                        subTask.setReporterEmail(fields.getReporter().getEmailAddress());
                        subTask.setReporterDisplayName(fields.getReporter().getDisplayName());
                    }

                    // Creator
                    if (fields.getCreator() != null) {
                        subTask.setCreatorEmail(fields.getCreator().getEmailAddress());
                        subTask.setCreatorDisplayName(fields.getCreator().getDisplayName());
                    }

                    // Estimation et temps
                    if (fields.getTimeestimate() != null) {
                        subTask.setEstimated(fields.getTimeestimate() / 3600f); // Conversion en heures
                    }

                    // Temps passé directement sur cette sous-tâche
                    if (fields.getTimespent() != null) {
                        System.out.println("time spent en secondes: " + fields.getTimespent());
                        subTask.setLoggedHours(fields.getTimespent());
                    }

                    // Calcul du temps restant
                    if (fields.getTimeestimate() != null && fields.getTimespent() != null) {
                        subTask.setRemaining(Math.max(0, (fields.getTimeestimate() - fields.getTimespent()) / 3600f));
                    }
                }
            }
            // Si c'est une sous-tâche basique (de la liste subtasks d'une tâche)
            else if (source instanceof TaskJiraDto.SubTask) {
                TaskJiraDto.SubTask subTaskDto = (TaskJiraDto.SubTask) source;

                if (subTaskDto != null && subTaskDto.getFields() != null) {
                    TaskJiraDto.SubTaskFields fields = subTaskDto.getFields();

                    // Informations de base
                    subTask.setSubTaskKey(subTaskDto.getKey());
                    subTask.setUrl(subTaskDto.getSelf());
                    subTask.setParentTask(parentTask);

                    // Summary
                    if (fields.getSummary() != null) {
                        subTask.setSummary(fields.getSummary());
                    }

                    // Statut
                    if (fields.getStatus() != null && fields.getStatus().getName() != null) {
                        subTask.setStatus(mapStatusFromJira(fields.getStatus().getName()));
                    }

                    // Priorité
                    if (fields.getPriority() != null && fields.getPriority().getName() != null) {
                        subTask.setPriority(mapPriorityFromJira(fields.getPriority().getName()));
                    }

                    // Type
                    if (fields.getIssuetype() != null) {
                        subTask.setIssueTypeName(fields.getIssuetype().getName());
                        subTask.setIssueTypeSubTask(fields.getIssuetype().isSubtask());
                        // Si vous avez un champ type dans SubTask
                        try {
                            subTask.getClass().getDeclaredField("type");
                            subTask.setType(fields.getIssuetype().getName());
                        } catch (NoSuchFieldException e) {
                            // Ne rien faire si le champ n'existe pas
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Type non pris en charge pour la conversion: " +
                        (source != null ? source.getClass().getName() : "null"));
            }

            // Initialiser des listes vides pour les checklists (commun aux deux branches)
            subTask.setChecklistInput(new ArrayList<>());
            subTask.setChecklistOutput(new ArrayList<>());

        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion en sous-tâche: " + e.getMessage());
            e.printStackTrace();
        }

        return subTask;
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

    /**
     * Traite et sauvegarde les sous-tâches pour une tâche parent donnée
     */
    public int processAndSaveSubTasks(List<TaskJiraDto.SubTask> subTasksDto, Task parentTask) {
        int count = 0;

        if (subTasksDto == null || subTasksDto.isEmpty()) {
            return 0;
        }

        for (TaskJiraDto.SubTask subTaskDto : subTasksDto) {
            SubTask subTask = convertToEntity(subTaskDto, parentTask);
            subTaskRepository.save(subTask);
            count++;
        }

        return count;
    }

    /**
     * Trouve toutes les sous-tâches pour une tâche parent donnée
     */
    public List<SubTask> findByParentTask(Task parentTask) {
        return subTaskRepository.findByParentTask(parentTask);
    }

    /**
     * Trouve une sous-tâche par sa clé
     */
    public SubTask findBySubTaskKey(String subTaskKey) {
        return subTaskRepository.findBySubTaskKey(subTaskKey);
    }

    /**
     * Supprime toutes les sous-tâches d'une tâche parent
     */
    public void deleteByParentTask(Task parentTask) {
        subTaskRepository.deleteByParentTask(parentTask);
    }
}
