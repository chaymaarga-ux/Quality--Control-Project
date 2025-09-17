package com.example.qualitycontrolproject;


import com.example.qualitycontrolproject.Services.FilterService;
import com.example.qualitycontrolproject.Services.ProjectService;

import com.example.qualitycontrolproject.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



//exécuter le code auto au démarrage de l'application Spring Boot.
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FilterService filterService;




    @Override
    public void run(String... args) throws Exception {
       // projectService.fetchAndInsertProjects();
      //  taskService.fetchAndInsertTasks();
//        filterService.executeAllFilters();
      //  filterService.insertDefaultFilters();

    }


}
