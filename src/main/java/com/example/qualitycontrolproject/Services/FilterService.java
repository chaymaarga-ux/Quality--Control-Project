package com.example.qualitycontrolproject.Services;


import com.example.qualitycontrolproject.Repository.FilterRepository;
import com.example.qualitycontrolproject.entities.Filter;
import com.example.qualitycontrolproject.entities.Task;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FilterService {
    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmailService emailService;


    public FilterService(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }



    public void insertDefaultFilters() {
        Map<String, String> filters = Map.ofEntries(
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
        );

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String code = entry.getKey();
            String query = entry.getValue();
            if (!filterRepository.existsByCode(entry.getKey())) {
                Filter filter = new Filter();
                filter.setCode(entry.getKey());
                filter.setQuery(entry.getValue());
                filterRepository.save(filter);
                System.out.println("✅ Filtre inséré : " + code);
            } else {
                System.out.println("ℹ️ Filtre déjà existant : " + code);
            }
        }
        System.out.println("✅ Filtres par défaut insérés");
    }
    // Get all filters
    public List<Filter> getAllFilters() {
        List<Filter> filters = filterRepository.findAll();
        System.out.println("Données récupérées : " + filters);
        return filters;
    }

    // Get filter by ID
    public Optional<Filter> getFilterById(Long id) {
        return filterRepository.findById(id);
    }

    // Get filter by name
    public Filter getFilterByName(String name) {
        return filterRepository.findByName(name);
    }

    //  Find filters by keyword in query
    public List<Filter> searchFiltersByQuery(String keyword) {
        return filterRepository.findByQueryContaining(keyword);
    }

    public Filter createFilter(Filter filter) {
        // Valider que les champs obligatoires sont présents
        if (filter.getName() == null || filter.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom du filtre est obligatoire");
        }

        if (filter.getQuery() == null || filter.getQuery().isEmpty()) {
            throw new IllegalArgumentException("La requête du filtre est obligatoire");
        }

        return filterRepository.save(filter);
    }

/*
    public void executeAllFilters() {
        List<Filter> filters = filterRepository.findAll();

        for (Filter filter : filters) {
            try {
                // Exécuter la requête
                List<Task> tasks = entityManager.createQuery(filter.getQuery(), Task.class)
                        .getResultList();

                // Si des tâches sont trouvées, envoyer un email
                if (!tasks.isEmpty()) {
                    String to = "chaymaa.aittarga.st@nttdata.com";
                    String cc = "";
                    String subject = "Alerte: " + filter.getName();
                    String firstName = "Équipe";
                    String lastName = "Qualité";

                    // Construire le corps du message
                    StringBuilder body = new StringBuilder();
                    body.append("Le filtre '").append(filter.getName()).append("' a trouvé ").append(tasks.size()).append(" tâche(s):<br><br>");
                    // Liste des tâches
                    for (Task task : tasks) {
                        body.append("- ").append(task.getTaskKey()).append(": ")
                                .append(task.getSummary()).append(" (").append(task.getStatus()).append(")<br>");
                    }

                    // Envoyer l'email
                    emailService.sendEmail(to, cc, subject, firstName, lastName, body.toString());
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'exécution du filtre " + filter.getName() + ": " + e.getMessage());
            }
        }
    }
*/
                    // Update an existing filter
    public Optional<Filter> updateFilter(Long id, Filter updatedFilter) {
        return filterRepository.findById(id).map(existingFilter -> {

            if (updatedFilter.getName() != null && !updatedFilter.getName().equals(existingFilter.getName())) {
                existingFilter.setName(updatedFilter.getName());
            }
            if (updatedFilter.getQuery() != null && !updatedFilter.getQuery().equals(existingFilter.getQuery())) {
                existingFilter.setQuery(updatedFilter.getQuery());
            }
            if (updatedFilter.getDescription() != null && !updatedFilter.getDescription().equals(existingFilter.getDescription())) {
                existingFilter.setDescription(updatedFilter.getDescription());
            }

            return filterRepository.save(existingFilter);
        });
    }
    //  Delete a filter
    public void deleteFilter(Long id) {
        filterRepository.deleteById(id);
    }


}
