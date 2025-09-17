package com.example.qualitycontrolproject.Services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(String to, String cc, String subject, String firstName, String lastName, String bodyMessage) {
        try {
            // Load the HTML template
            String template = loadTemplate("template.html");

            // Replace placeholders
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("firstName", firstName != null ? firstName : "");
            placeholders.put("lastName", lastName != null ? lastName : "");
           // placeholders.put("bodyMessage", bodyMessage);
            placeholders.put("bodyMessage", generateHtmlTable(bodyMessage));

            String htmlContent = replacePlaceholders(template, placeholders);

            // Create the email message
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc);
            }
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // Embed images
            InputStream nttLogoStream = getClass().getClassLoader().getResourceAsStream("images/img.png");
            URL url = getClass().getClassLoader().getResource("images/NTTDATALOGO.png");
            System.out.println(url);

            InputStream qualityControlAppStream = getClass().getClassLoader().getResourceAsStream("images/qualityControlApp.png");

            if (nttLogoStream != null && qualityControlAppStream != null) {
                helper.addInline("nttLOGO", new ByteArrayResource(nttLogoStream.readAllBytes()), "image/png");
               // helper.addInline("qualityControlApp", new ByteArrayResource(qualityControlAppStream.readAllBytes()), "image/png");
            } else {
                throw new IOException("One or more image files not found.");
            }

            emailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loadTemplate(String path) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("Template file not found: " + path);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String replacement = entry.getValue() != null ? entry.getValue() : "";
            template = template.replace("{{ " + entry.getKey() + " }}", replacement);
        }
        return template;
    }

    private String generateHtmlTable(String rawBodyMessage) {
        // Si le message contient déjà du HTML, ne rien faire
        if (rawBodyMessage.contains("<table")) return rawBodyMessage;

        // Sinon, on considère que rawBodyMessage contient une version CSV-like à parser
        // Exemple : "Task1;Closed;RDM;Checklist missing;Upload it;John Doe\nTask2;In Progress;PLAN;..."
        String[] lines = rawBodyMessage.split("\n");
        StringBuilder sb = new StringBuilder();

        sb.append("<table border='1' style='border-collapse: collapse; width: 100%; font-family: Arial;'>")
                .append("<thead style='background-color: #0b66bc; color: white;'>")
                .append("<tr>")
                .append("<th>Id Task</th><th>Status of the task</th><th>Manual audit question Code</th><th>CMMI Process</th><th>Non- Conformities Description</th><th>Corrective Actions</th><th>Responsible</th>")
                .append("</tr></thead><tbody>");

        for (String line : lines) {
            String[] cols = line.split(";");
            sb.append("<tr>");
            for (String cell : cols) {
                sb.append("<td>").append(cell.trim()).append("</td>");
            }
            sb.append("</tr>");
        }

        sb.append("</tbody></table>");
        return sb.toString();
    }


 /*   public void sendSimpleEmail(String to, String subject, String text) {
        try {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("usr_audit_automation@emeal.nttdata.com");
                message.setTo(to);
                message.setSubject(subject);
                message.setText(text);
                emailSender.send(message);
                System.out.println("✅ Email envoyé avec succès à " + to);
            } catch (Exception e) {
                System.out.println("❌ Échec de l'envoi d'email : " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

}
