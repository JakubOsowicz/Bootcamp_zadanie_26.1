package pl.osowicz.task_manager.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.user WHERE t.status = ?1")
    List<Task> findAllByStatusEquals(Status status);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.user WHERE t.status <> ?1")
    List<Task> findAllByStatusIsNotOrderByDeadLine(Status status);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.user")
    List<Task> findAllTasks();
}
