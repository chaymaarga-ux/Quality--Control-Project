package com.example.qualitycontrolproject.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    private String role;
    private String businessUnit;
    private String service;
    private String responsable;

/*    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Filter> filters;*/

}


