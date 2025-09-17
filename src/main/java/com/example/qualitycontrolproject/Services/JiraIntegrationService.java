package com.example.qualitycontrolproject.Services;

import com.example.qualitycontrolproject.Repository.FilterRepository;
import com.example.qualitycontrolproject.dtos.ProjectJiraDto;
import com.example.qualitycontrolproject.entities.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JiraIntegrationService {

    private final RestTemplate restTemplate;
    private final String jiraToken;
    @Autowired
    private FilterRepository filterRepository;

    public JiraIntegrationService(@Value("${jira.api.token}") String jiraToken) {
        this.restTemplate = new RestTemplate();
        this.jiraToken = jiraToken;
    }

    public List<ProjectJiraDto> getAllProjects() {
        String url = "https://umane.emeal.nttdata.com/jiraito/rest/api/2/project";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jiraToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<ProjectJiraDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<ProjectJiraDto>>() {}
            );

            System.out.println("✅ Réponse de Jira : " + response.getBody());
            return response.getBody();

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'appel à Jira : " + e.getMessage());
            e.printStackTrace();
            return List.of(); // ou return null; selon ta gestion
        }
    }

    //Elle récupère toutes les issues (tâches et sous-tâches) du projet dont la clé est projectKey.
    public Object getIssuesForProject(String projectKey) {
        String url = "https://umane.emeal.nttdata.com/jiraito/rest/api/2/search?jql=project=" + projectKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jiraToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Object.class
        );

        return response.getBody();
    }

    public Object getParentTasksForProject(String projectKey) {
        String url = "https://umane.emeal.nttdata.com/jiraito/rest/api/2/search?jql=project="
                + projectKey + " AND issuetype NOT IN subTaskIssueTypes()"  + "&maxResults=100";


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jiraToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }


    public Object getSubTasksForProject(String projectKey) {
        String url = "https://umane.emeal.nttdata.com/jiraito/rest/api/2/search?jql=project=" + projectKey + " AND issuetype in subTaskIssueTypes()";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jiraToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Object.class
        );

        return response.getBody();
    }

    public Object getSubTasksOfTask(String parentTaskKey) {
        String url = "https://umane.emeal.nttdata.com/jiraito/rest/api/2/search?jql=parent=" + parentTaskKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jiraToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getBody();
    }

    public Object getProjectDetails(String projectKey) {
        String url = "https://umane.emeal.nttdata.com/jiraito/rest/api/2/project/" + projectKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jiraToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));


        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Object.class);

        return response.getBody();
    }

    //Récupère les détails complets d'une tâche ou sous-tâche spécifique
    public Object getIssueDetails(String issueKey) {
            String url = "https://umane.emeal.nttdata.com/jiraito/rest/api/2/issue/" + issueKey;
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jiraToken);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));


            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    Object.class);

            return response.getBody();
    }


  /*  private final Map<String, String> predefinedFilters = Map.ofEntries(
            Map.entry("1",    "status = \"In Progress\" AND Issuetype in (\"Corrective\", \"Evolutive\") AND \"Checklist Input\" is EMPTY AND created >= 2024-06-01 ORDER BY created DESC"),
            Map.entry("2",    "status in (Delivered, Closed) AND Issuetype in (\"Corrective\", \"Evolutive\") AND \"Checklist Output\" is EMPTY AND created >= 2024-06-01 ORDER BY created DESC"),
            Map.entry("3",    "issuetype = Evolutive AND status in (Closed, Delivered) AND resolution != Canceled AND \"Total horas incurridas\" > 100 AND \"estimation\" = \"Total horas incurridas\""),
            Map.entry("4",    "issuetype = Evolutive AND issueFunction in parentsOf(\"issuetype in subTaskIssueTypes() and status not in (closed) and issueFunction in subtasksOf('issuetype in standardIssueTypes() and status in (Closed)')\")"),
            Map.entry("5",    "issuetype in (Evolutive) AND status in (Delivered, Closed) AND \"Sale estimate (h)\" = 0"),
            Map.entry("6",    "issuetype in (Evolutive) AND status in (Delivered, Closed) AND \"Internal estimate (h)\" = 0"),
            Map.entry("7",    "issuetype = Evolutive AND status in (Closed, Delivered) AND \"Total horas incurridas\" > 20"),
            Map.entry("8",    "issuetype = Evolutive AND status in (Closed, Delivered) AND \"Total horas incurridas\" > 20"),
            Map.entry("9",    "issuetype = Evolutive AND status in (Closed, Delivered) AND \"Change in Scope\" is not EMPTY AND created >= 2024-06-01"),

            Map.entry("10.1", "issueFunction in issueFieldMatch(\"project = INDITEX AND issuetype = Evolutive AND status in (Closed, Delivered)\", \"resolution\", \"Resuelta\") AND issueFunction in dateCompare(\"\", \"Fecha de Creación <= Expected start date\") AND issueFunction in dateCompare(\"\", \"Expected start date <= Expected delivery date\")"),
            Map.entry("10.2", "issueFunction in subtasksOf(\"project = MDPAM AND issuetype = Evolutive AND status in (Closed, Delivered) AND resolution = Resuelta\") AND issueFunction in dateCompare(\"\", \"Fecha de Creación <= Sub-Expected start date\") AND issueFunction in dateCompare(\"\", \"Sub-Expected start date <= Sub-Expected delivery date\") AND issueFunction in dateCompare(\"\", \"Sub-Expected start date <= Sub-Real delivery date\")"),
            Map.entry("10.3", "issueFunction in subtasksOf(\"project = MDPAM AND issuetype = Evolutive AND status in (Closed, Delivered) AND resolution = Resuelta\") AND issueFunction in issueFieldMatch(\"issuetype = Sub-Management\", \"Subtask Expected start date\" <= Expected start date) AND issueFunction in issueFieldMatch(\"issuetype = Sub-Management\", \"Subtask Real end date\" <= Real delivery date)"),

            Map.entry("11",   "issuetype in (\"Sub-Acceptance Tests\", \"Sub-Crossed Test\", \"Sub-Functional System Tests\", \"Sub-Integrated Tests\", \"Sub-Non Functional System Tests\", \"Sub-Unit Tests\") AND status in (Closed, Delivered) AND created >= -26w"),
            Map.entry("12",   "issuetype = Evolutive AND status in (Closed, Delivered) AND \"Total horas incurridas\" > 40"),
            Map.entry("13",   "issueFunction in issueFieldMatch(\"project = MDPAM AND issuetype = Evolutive AND status in (Closed, Delivered) AND resolution != Cancelled\", \"Sub-Construction\", \"Sub-Peer review\") AND issueFunction in subtasksOf(\"project = MDPAM AND issuetype = Sub-Construction\") AND \"Total horas incurridas\" > 100"),
            Map.entry("14",   "issuetype = Sub-Defect AND created >= 2020-06-01"),
            Map.entry("15",   "issuetype = Sub-Defect AND created >= 2020-06-01"),
            Map.entry("16",   "issuetype = Sub-Defect AND status in (\"Delivered\", \"Closed\") AND Origin = External AND created >= 2020-06-01"),
            Map.entry("17",   "issuetype = Evolutive AND status in (Closed, Delivered) AND \"Total horas incurridas\" > 40 AND issueLinkType is EMPTY"),

            // Les deux derniers :
            Map.entry("18",   "issuetype = Evolutive AND status in (Closed, Delivered) AND \"Total horas incurridas\" > 40 AND \"issueLinkType\" is EMPTY"),  // Exemple – à adapter selon le vrai filtre
            Map.entry("19",   "issuetype = Evolutive AND status in (Closed, Delivered) AND \"Total horas incurridas\" > 40 AND created >= 2024-06-01"),
            Map.entry("20",   "issuetype = Evolutive") //je teste
    );*/



 /*   public  Map<String, List<Map<String, Object>>>  searchIssuesWithCode(String filterCode, int maxResults) {
        String jql = predefinedFilters.get(filterCode);
        if (jql == null) {
            throw new IllegalArgumentException("Code de filtre inconnu : " + filterCode);
        }

        List<ProjectJiraDto> projects = getAllProjects();
        Map<String, List<Map<String, Object>>> resultByProject = new HashMap<>();
        for (ProjectJiraDto project : projects) {
            String projectKey = project.getKey();

            // Si le JQL contient "project =", on le remplace dynamiquement
            String jqlC = jql.contains("project =") ?
                    jql.replaceFirst("project = [^\"]*|project = \"[^\"]*\"", "project = " + projectKey) :
                    "project = " + projectKey + " AND " + jql;

            List<Map<String, Object>> issues = searchIssuesWithJQL(jqlC, maxResults);
            resultByProject.put(projectKey, issues);
        }

        return resultByProject;
    }*/
 public Map<String, List<Map<String, Object>>> searchIssuesWithCode(String filterCode, int maxResults) {
     System.out.println("🔍 Reçu code filtre: " + filterCode);

   //  String jql = predefinedFilters.get(filterCode);
     Filter filter = filterRepository.findByCode(filterCode);

     if (filter == null) {
         System.err.println("❌ Filtre introuvable: " + filterCode);
         throw new IllegalArgumentException("Code de filtre inconnu : " + filterCode);
     }

     List<ProjectJiraDto> projects = getAllProjects();
     System.out.println("📦 Projets récupérés: " + projects.size());

     Map<String, List<Map<String, Object>>> resultByProject = new HashMap<>();

     for (ProjectJiraDto project : projects) {
         String projectKey = project.getKey();
         String query = filter.getQuery();
         String jqlC = query.contains("project =")
                 ? query.replaceFirst("project = [^\"]*|project = \"[^\"]*\"", "project = " + projectKey)
                 : "project = " + projectKey + " AND " + query;

         System.out.println("🔄 Projet: " + projectKey + " | JQL: " + jqlC);

         try {
             List<Map<String, Object>> issues = searchIssuesWithJQL(jqlC, maxResults);
             resultByProject.put(projectKey, issues);
             // Ajoute une pause de 500 ms entre les appels
             try {
                 Thread.sleep(500); // pour éviter les 429
             } catch (InterruptedException e) {
                 Thread.currentThread().interrupt(); // bonne pratique
             }
         } catch (Exception e) {
             System.err.println("❌ Erreur pour le projet " + projectKey + " avec JQL: " + jqlC);
             e.printStackTrace();
         }
     }

     return resultByProject;
 }




    public Map<String, Map<String, List<Map<String, Object>>>> searchAllFiltersGroupedByProject(int maxResults) {
        List<ProjectJiraDto> projects = getAllProjects();
        List<Filter> filters = filterRepository.findAll();

        Map<String, Map<String, List<Map<String, Object>>>> results = new HashMap<>();

        for (ProjectJiraDto project : projects) {
            String projectKey = project.getKey();
            if (projectKey == null || projectKey.isBlank()) {
                System.err.println("⚠️ Projet sans clé ignoré");
                continue;
            }

            Map<String, List<Map<String, Object>>> projectResults = new HashMap<>();

            for (Filter filter : filters) {
                String filterCode = filter.getCode();
                if (filterCode == null || filterCode.isBlank()) {
                    System.err.println("⚠️ Filtre sans code ignoré");
                    continue;
                }

                String query = filter.getQuery();
                String jql = query.contains("project =")
                        ? query.replaceFirst("project = [^\"]*|project = \"[^\"]*\"", "project = " + projectKey)
                        : "project = " + projectKey + " AND " + query;

                try {
                    List<Map<String, Object>> issues = searchIssuesWithJQL(jql, maxResults);
                    projectResults.put(filterCode, issues);
                    Thread.sleep(300); // pour éviter les erreurs 429
                } catch (Exception e) {
                    System.err.println("❌ Erreur projet " + projectKey + ", filtre " + filterCode);
                    e.printStackTrace();
                    projectResults.put(filterCode, List.of()); // insère une liste vide si erreur
                }
            }

            results.put(projectKey, projectResults);
        }

        return results;
    }
























    public List<Map<String, Object>> searchAllFiltersForAllProjects(int maxResults) {
        List<ProjectJiraDto> projects = getAllProjects();
        List<Filter> filters = filterRepository.findAll();

        List<Map<String, Object>> results = new ArrayList<>();

        for (ProjectJiraDto project : projects) {
            String projectKey = project.getKey();

            for (Filter filter : filters) {
                String query = filter.getQuery();
                String jql = query.contains("project =")
                        ? query.replaceFirst("project = [^\"]*|project = \"[^\"]*\"", "project = " + projectKey)
                        : "project = " + projectKey + " AND " + query;

                try {
                    List<Map<String, Object>> issues = searchIssuesWithJQL(jql, maxResults);
                    for (Map<String, Object> issue : issues) {
                        Map<String, Object> result = new HashMap<>();
                        result.put("project", project);
                        result.put("filter", filter);
                        result.put("issue", issue);
                        results.add(result);
                    }

                    Thread.sleep(500);
                } catch (Exception e) {
                    System.err.println("❌ Erreur pour projet " + projectKey + " avec filtre " + filter.getCode());
                    e.printStackTrace();
                }
            }
        }

        return results;
    }

    public String buildEmailBodyFromResults(Map<String, Map<String, List<Map<String, Object>>>> groupedResults) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Map<String, List<Map<String, Object>>>> projectEntry : groupedResults.entrySet()) {
            String projectKey = projectEntry.getKey();
            Map<String, List<Map<String, Object>>> filters = projectEntry.getValue();

            for (Map.Entry<String, List<Map<String, Object>>> filterEntry : filters.entrySet()) {
                String filterCode = filterEntry.getKey();
                List<Map<String, Object>> issues = filterEntry.getValue();

                CmmiInfo cmmiInfo = cmmiMapping.getOrDefault(filterCode, new CmmiInfo("N/A", "No corrective action defined"));

                for (Map<String, Object> issue : issues) {
                    String taskId = (String) issue.get("key");
                    Map<String, Object> fields = (Map<String, Object>) issue.get("fields");
                    String status = fields.get("status") != null ? ((Map<?, ?>) fields.get("status")).get("name").toString() : "N/A";
                    String summary = fields.get("summary") != null ? fields.get("summary").toString() : "No description";
                    Map<String, Object> reporter = (Map<String, Object>) fields.get("reporter");
                    String reporterName = reporter != null ? reporter.get("displayName").toString() : "N/A";


                    // Ligne CSV-like : TaskId;Status;Code;Filter;Description;Action;Responsible
                    sb.append(taskId).append(";")
                            .append(status).append(";")
                            .append(filterCode).append(";")
                            .append(cmmiInfo.getCmmiProcess()).append(";")
                            .append(summary).append(";")
                            .append(cmmiInfo.getCorrectiveAction()).append(";")
                            .append(reporterName).append("\n");
                }
            }
        }

        return sb.toString();
    }
























    public List<Map<String, Object>> searchIssuesWithJQL(String jql, int maxResults) {
        String url = "https://umane.emeal.nttdata.com/jiraito/rest/api/2/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jiraToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jql", jql);
        requestBody.put("maxResults", maxResults);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getBody() != null) {
            return (List<Map<String, Object>>) response.getBody().get("issues");
        }
        return new ArrayList<>();
    }

    public static class CmmiInfo {
        private String cmmiProcess;
        private String correctiveAction;
        private String description;

        public CmmiInfo(String cmmiProcess, String correctiveAction, String description) {
            this.cmmiProcess = cmmiProcess;
            this.correctiveAction = correctiveAction;
            this.description = description;
        }

        public String getCmmiProcess() {
            return cmmiProcess;
        }

        public String getCorrectiveAction() {

            return correctiveAction;
        }
        public String getDescription() {
            return description;
        }
    }

    // Dans JiraIntegrationService
    private static final Map<String, CmmiInfo> cmmiMapping = Map.ofEntries(
            Map.entry("1", new CmmiInfo("RDM", "It must be attached in sharepoint","The necessary documentation for the parent task is attached in the task management tool or in the corresponding repository.")),
            Map.entry("2", new CmmiInfo("RDM", "Upload the input and output checklist and also validate that the input checklist is answered.", "The input checklist is completed.")),
            Map.entry("3", new CmmiInfo("VV", "Upload the input and output checklist and also validate that the input checklist is answered.", "The output checklist is completed.")),
            Map.entry("4", new CmmiInfo("EST", "Review subtask status vs parent status.", "Checklist input missing for in-progress tasks")),
            Map.entry("5", new CmmiInfo("PLAN", "Check sale estimate is filled.", "Checklist input missing for in-progress tasks")),
            Map.entry("6", new CmmiInfo("EST", "Check internal estimate is filled.", "Checklist input missing for in-progress tasks")),
            Map.entry("7", new CmmiInfo("EST", "Attach the estimator in SharePoint.", "For evolutives requiring more than 20 hours, the estimator must be attached in the request management tool")),
            Map.entry("8", new CmmiInfo("EST", "Set the same estimate in the internal estimate and in the Estimated.", "The internal estimate does not match the sum of the sub-task estimates (Estimated)")),
            Map.entry("9", new CmmiInfo("EST,PLAN", "Document scope changes.", "Checklist input missing for in-progress tasks")),
            Map.entry("10.1", new CmmiInfo("PLAN", "Validate planning dates.", "\t\n" +
                    "The expected delivery date is < todays date.")),
            Map.entry("10.2", new CmmiInfo("PLAN", "Validate subtask delivery plan.", "Checklist input missing for in-progress tasks")),
            Map.entry("10.3", new CmmiInfo("PLAN", "Check consistency between sub and main task delivery.", "Checklist input missing for in-progress tasks")),
            Map.entry("11", new CmmiInfo("PLAN", "Ensure test evidence is attached.", "Checklist input missing for in-progress tasks")),
            Map.entry("12", new CmmiInfo("PLAN", "Effort control verification.", "Checklist input missing for in-progress tasks")),
            Map.entry("13", new CmmiInfo("PR", "Ensure peer review process followed.", "Checklist input missing for in-progress tasks")),
            Map.entry("14", new CmmiInfo("PR", "Analyze defect cause.", "Checklist input missing for in-progress tasks")),
            Map.entry("15", new CmmiInfo("PR", "Check validation coverage.", "Checklist input missing for in-progress tasks")),
            Map.entry("16", new CmmiInfo("VV", "Investigate external origin issues.", "Checklist input missing for in-progress tasks")),
            Map.entry("17", new CmmiInfo("VV", "Link issues appropriately.", "Checklist input missing for in-progress tasks")),
            Map.entry("18", new CmmiInfo(" ", "Verify issue linkage.", "Checklist input missing for in-progress tasks")),
            Map.entry("19", new CmmiInfo("PLAN", "Track excessive effort.", "Checklist input missing for in-progress tasks")),
    );



   /* public List<Map<String, Object>> searchIssuesWithFilter() {
        String url = "https://umane.emeal.nttdata.com/jiraito/rest/api/2/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jiraToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jql = "project = KST AND issuetype = Evolutive  AND status in (Closed, Delivered)";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("jql", jql);
        requestBody.put("maxResults", 100);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        List<Map<String, Object>> issues = new ArrayList<>();

        if (response.getBody() != null) {
            issues = (List<Map<String, Object>>) response.getBody().get("issues");
        }

        return issues;
    }
*/



}