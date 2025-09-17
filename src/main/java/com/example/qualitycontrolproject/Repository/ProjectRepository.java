package com.example.qualitycontrolproject.Repository;

import com.example.qualitycontrolproject.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByName(String name);
    Project findByProjectKey(String projectKey);

   /* @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Project> searchByName(@Param("searchTerm") String searchTerm);

    // Optional: Additional search methods
    @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.projectKey) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Project> searchByNameOrKeyOrType(@Param("searchTerm") String searchTerm);
*/

}
