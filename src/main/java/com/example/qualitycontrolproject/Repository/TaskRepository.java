package com.example.qualitycontrolproject.Repository;

import com.example.qualitycontrolproject.Enum.TaskStatus;
import com.example.qualitycontrolproject.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByTaskKey(String taskKey);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByAssignee(String assignee);

    List<Task> findByProjectProjectId(Long projectId);

    //option 2
   /* @Query("SELECT t FROM Task t WHERE t.project.projectId = :projectId")
    List<Task> findTasksByProjectId(@Param("projectId") Long projectId);
*/

}

