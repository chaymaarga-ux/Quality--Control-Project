package com.example.qualitycontrolproject.smtpTest;

import com.example.qualitycontrolproject.Services.EmailService;
import com.example.qualitycontrolproject.Services.JiraIntegrationService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.qualitycontrolproject")
public class SmtpConnectionTest {

    public static void main(String[] args) {
        SpringApplication.run(SmtpConnectionTest.class, args);
    }

    @Bean
    public CommandLineRunner testEmail(@Autowired EmailService emailService, @Autowired JiraIntegrationService jiraIntegrationService) {
        return args -> {
            System.out.println("Tentative d'envoi d'email de test...");


          /*  String to = "caittarg@emeal.nttdata.com";

            String cc = "";

            String subject = "Test d'envoi d'email avec template HTML depuis Spring Boot";

            String firstName = "Test Prénom";
            String lastName = "Test Nom";*/

          /*  String bodyMessage = "This is a test message that will be inserted into the HTML template. " +
                    "It demonstrates the functionality of sending emails with formatting and embedded images.";

            emailService.sendEmail(to, cc, subject, firstName, lastName, bodyMessage);*/


            int maxResults = 10;
            Map<String, Map<String, List<Map<String, Object>>>> results = jiraIntegrationService.searchAllFiltersGroupedByProject(maxResults);

            String body = jiraIntegrationService.buildEmailBodyFromResults(results);

            emailService.sendEmail(
                    "caittarg@emeal.nttdata.com",   // destinataire
                    "chaymaaaittarga2002@gmail.com",
                    "JIRA Non-Conformities Report",
                    "Dear",
                    "Team",
                    body // CSV-like string → converti automatiquement en <table> HTML
            );

            System.out.println("Opération terminée");
        };
    }
}