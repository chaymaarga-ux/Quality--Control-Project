package com.example.qualitycontrolproject.Repository;

import com.example.qualitycontrolproject.Enum.TaskStatus;
import com.example.qualitycontrolproject.entities.SubTask;
import com.example.qualitycontrolproject.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

    SubTask findBySubTaskKey(String subTaskKey);

    List<SubTask> findByStatus(TaskStatus status);

    List<SubTask> findByAssignee(String assignee);

    //Find SubTasks by Parent Task ID
    List<SubTask> findByParentTaskTaskId(Long taskId);

    List<SubTask> findByParentTask(Task parentTask);
    void deleteByParentTask(Task parentTask);
}
