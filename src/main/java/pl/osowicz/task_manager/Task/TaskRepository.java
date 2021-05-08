package pl.osowicz.task_manager.Task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAndStatusEquals(Status status);

    List<Task> findAllByStatusIsNotOrderByDeadLine(Status status);
}
