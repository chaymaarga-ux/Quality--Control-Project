package com.example.qualitycontrolproject.Services;


import com.example.qualitycontrolproject.Repository.ProjectRepository;
import com.example.qualitycontrolproject.dtos.ProjectJiraDto;
import com.example.qualitycontrolproject.entities.Project;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProjectService {
    @Autowired
    private final ProjectRepository projectRepository;
    @Autowired
    private JiraIntegrationService jiraIntegrationService;



    @Autowired
    public ProjectService(ProjectRepository projectRepository) { //@Lazy RestTemplate restTemplate,
       // this.restTemplate = restTemplate;
        this.projectRepository = projectRepository;
    }


    /**
     * Récupère et insère les projets depuis Jira
     * @return le nombre de nouveaux projets ajoutés
     */
    public int fetchAndInsertProjects() {
        System.out.println("Starting project synchronization with Jira...");
        int newProjects = 0;

        try {
            // Récupérer les projets depuis Jira
            List<ProjectJiraDto> jiraProjects = jiraIntegrationService.getAllProjects();
            System.out.println("Récupéré " + jiraProjects.size() + " projets de Jira");

            // Traiter chaque projet
            for (ProjectJiraDto jiraDto : jiraProjects) {


                // Récupérer les détails du projet pour obtenir le lead
                try {
                    Object projectDetails = jiraIntegrationService.getProjectDetails(jiraDto.getKey());

                    if (projectDetails instanceof Map) {
                        Map<String, Object> detailsMap = (Map<String, Object>) projectDetails;
                        Map<String, Object> leadMap = (Map<String, Object>) detailsMap.getOrDefault("lead", Collections.emptyMap());

                        // Créer et définir le LeadDto
                        ProjectJiraDto.LeadDto leadDto = new ProjectJiraDto.LeadDto();
                        leadDto.setDisplayName((String) leadMap.getOrDefault("displayName", "NA"));
                        System.out.println("le lead" +leadDto);

                        jiraDto.setLeadDto(leadDto);
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération du lead pour le projet " + jiraDto.getKey() + ": " + e.getMessage());
                    // Créer un leadDto par défaut
                    ProjectJiraDto.LeadDto leadDto = new ProjectJiraDto.LeadDto();
                    leadDto.setDisplayName("NA");
                    jiraDto.setLeadDto(leadDto);
                }


                // Convertir en entité Project
                Project project = convertToEntity(jiraDto);

                // Vérifier si le projet existe déjà
                Project existingProject = getProjectByKey(project.getProjectKey());
                if (existingProject == null) {
                    // Sauvegarder le nouveau projet
                    insertProject(project);
                    System.out.println("Projet créé: " + project.getName() + " (" + project.getProjectKey() + ")");
                    newProjects++;
                } else {
                    System.out.println("Projet existant ignoré: " + project.getName());
                }
            }

            System.out.println("Synchronisation terminée: " + newProjects + " nouveaux projets ajoutés");

        } catch (Exception e) {
            System.err.println("Erreur durant la synchronisation: " + e.getMessage());
            e.printStackTrace();
        }

        return newProjects;
    }

    /**
     * Convertit un DTO Jira en entité Project
     */
    private Project convertToEntity(ProjectJiraDto jiraDto) {
        Project project = new Project();

        try {
            if (jiraDto != null) {
                if (jiraDto.getName() != null) {
                    project.setName(jiraDto.getName());
                }

                if (jiraDto.getKey() != null) {
                    project.setProjectKey(jiraDto.getKey());
                }

                if (jiraDto.getProjectTypeKey() != null) {
                    project.setType(jiraDto.getProjectTypeKey());
                }

                if (jiraDto.getSelf() != null) {
                    project.setUrl(jiraDto.getSelf());
                }

                if (jiraDto.getProjectCategory() != null) {
                    if (jiraDto.getProjectCategory().getName() != null) {
                        project.setCategory(jiraDto.getProjectCategory().getName());
                    }
                    if (jiraDto.getProjectCategory().getDescription() != null) {
                        project.setDescription(jiraDto.getProjectCategory().getDescription());
                    }
                }
                if (jiraDto.getLeadDto() != null) {
                    if (jiraDto.getLeadDto().getDisplayName() != null) {
                        project.setLead(jiraDto.getLeadDto().getDisplayName());
                        System.out.println("Lead assigné dans convertToEntity: " + project.getLead());

                    }
                }



                project.setTasks(new ArrayList<>());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion du projet: " + e.getMessage());
        }

        return project;
    }




    //insérer un projet
    @Transactional
    public Project insertProject(Project newProject) {
        return projectRepository.save(newProject);
    }

    //  Get all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    //  Get project by ID
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    // Get project by Key
    public Project getProjectByKey(String projectKey) {
        return projectRepository.findByProjectKey(projectKey);
    }

    //  Get project by Name
    public Project getProjectByName(String name) {
        return projectRepository.findByName(name);
    }

    //  Save a new project
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    //  Update an existing project
    public Optional<Project> updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id).map(existingProject -> {

            if (updatedProject.getName() != null && !updatedProject.getName().equals(existingProject.getName())) {
                existingProject.setName(updatedProject.getName());
            }
            if (updatedProject.getProjectKey() != null && !updatedProject.getProjectKey().equals(existingProject.getProjectKey())) {
                existingProject.setProjectKey(updatedProject.getProjectKey());
            }
            if (updatedProject.getType() != null && !updatedProject.getType().equals(existingProject.getType())) {
                existingProject.setType(updatedProject.getType());
            }
            if (updatedProject.getUrl() != null && !updatedProject.getUrl().equals(existingProject.getUrl())) {
                existingProject.setUrl(updatedProject.getUrl());
            }
            if (updatedProject.getLead() != null && !updatedProject.getLead().equals(existingProject.getLead())) {
                existingProject.setLead(updatedProject.getLead());
            }
            if (updatedProject.getCategory() != null && !updatedProject.getCategory().equals(existingProject.getCategory())) {
                existingProject.setCategory(updatedProject.getCategory());
            }
            if (updatedProject.getDescription() != null && !updatedProject.getDescription().equals(existingProject.getDescription())) {
                existingProject.setDescription(updatedProject.getDescription());
            }

            return projectRepository.save(existingProject);
        });
    }

    //  Delete a project
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

   /* // Search projects by name
    public List<Project> searchProjectsByName(String searchTerm) {
        return projectRepository.searchByName(searchTerm);
    }

    // Advanced search across multiple fields
    public List<Project> searchProjects(String searchTerm) {
        return projectRepository.searchByNameOrKeyOrType(searchTerm);
    }*/

}
