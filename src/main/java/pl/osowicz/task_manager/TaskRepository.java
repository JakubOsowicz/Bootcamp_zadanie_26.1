package pl.osowicz.task_manager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAndStatusEquals(Status status);

    List<Task> findAllByStatusIsNot(Status status);
}
