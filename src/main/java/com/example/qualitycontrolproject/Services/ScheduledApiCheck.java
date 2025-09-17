package com.example.qualitycontrolproject.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledApiCheck {
//    @PostConstruct
//    public void init() {
//        runDailyTask();
//    }


    @Autowired
    private ProjectService projectService;
    private TaskService taskService;
    private FilterService filterService;



    // Exécute la synchronisation des projets tous les jours à 8h00
    @Scheduled(cron = "0 0 8 * * ?")
    public void runDailyTask() {
        System.out.println("Starting scheduled project synchronization...");
        projectService.fetchAndInsertProjects();
        taskService.fetchAndInsertTasks();
//        filterService.executeAllFilters();

    }
}


