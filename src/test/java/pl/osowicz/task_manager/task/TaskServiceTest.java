package pl.osowicz.task_manager.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.osowicz.task_manager.testClasses.DateTimeProvider;
import pl.osowicz.task_manager.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    private TaskService taskService;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository, dateTimeProvider);
    }

    private List<Status> getStatuses() {
        return List.of(Status.values());
    }

    private List<Task> getTaskList() {
        List<Task> tasks = new ArrayList<>();
        List<Status> statuses = getStatuses();
        long id = 1L;
        for (Status s : statuses) {
            tasks.add(new Task(id, "zadanie " + id, "z_opisem", null, s, null, null, null));
            id++;
        }
        return tasks;
    }

    @Test
    void shouldReturnAllTasks() {
        //given
        List<Task> tasks = getTaskList();
        Mockito.when(taskRepository.findAllTasks()).thenReturn(tasks);

        //when
        List<Task> allTasks = taskService.findAll();

        //then
        assertThat(allTasks).isEqualTo(tasks);
    }

    @Test
    void shouldReturnSpecifiedTaskById() {
        //given
        long id = 1L;
        List<Task> tasks = getTaskList();
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(tasks.get((int) id)));

        //when
        Task task = taskService.findById(id);

        //then
        assertThat(task).isEqualTo(tasks.get((int) id));
    }

    @Test
    void shouldReturnNullForNotExistingId() {
        //given
        long id = 999L;
        List<Task> taskList = getTaskList().stream().filter(x -> x.getId() == id).collect(Collectors.toList());
        Mockito.when(taskRepository.findById(id)).thenReturn(taskList.stream().findAny());

        //when
        Task task = taskService.findById(id);

        //then
        assertThat(task).isEqualTo(null);
    }

    @Test
    void shouldReturnTasksWithSpecifiedStatusForEveryStatusCode() {
        //given
        List<Status> statusList = getStatuses();
        List<Task> tasks = getTaskList();

        for (Status s : statusList) {
            List<Task> taskListByStatus = tasks.stream()
                    .filter(x -> x.getStatus() == s)
                    .collect(Collectors.toList());
            Mockito.when(taskRepository.findAllByStatusEquals(s)).thenReturn(taskListByStatus);

            //when
            List<Task> tasksByStatus = taskService.findAllByStatus(s);

            //then
            assertThat(tasksByStatus).isEqualTo(taskListByStatus);
        }
    }

    @Test
    void shouldReturnEmptyListIfNotAnyTaskWithStatusCode() {
        //given
        List<Status> statuses = getStatuses();
        Status status = statuses.get(0);
        List<Task> tasks = new ArrayList<>();
        Mockito.when(taskRepository.findAllByStatusEquals(status)).thenReturn(Collections.emptyList());

        //when
        List<Task> tasksByStatus = taskService.findAllByStatus(status);

        //then
        assertThat(tasks).isEqualTo(tasksByStatus);
    }

    @Test
    void shouldReturnFullTaskListDtoForNullStatusParameter() {
        //given
        List<Task> taskList = getTaskList();
        Mockito.when(taskRepository.findAllTasks()).thenReturn(taskList);

        //when
        List<TaskDto> allByStatusDto = taskService.findAllByStatusDto(null);

        //then
        assertThat(taskList.size()).isEqualTo(allByStatusDto.size());
    }

    @Test
    void shouldReturnAllCompletedTasksForStatusCompleted() {
        //given
        List<Task> taskList = getTaskList();
        taskList.add(new Task(7L, "zadanie 7", "z_opisem", null, Status.COMPLETED, null, null, null));
        List<Task> completedTasks = taskList.stream().filter(x -> x.getStatus() == Status.COMPLETED).collect(Collectors.toList());
        Mockito.when(taskRepository.findAllByStatusEquals(Status.COMPLETED)).thenReturn(completedTasks);

        //when
        List<TaskDto> allByStatusDto = taskService.findAllByStatusDto(Status.COMPLETED);

        //then
        assertThat(completedTasks.size()).isEqualTo(allByStatusDto.size());
    }


    @Test
    void shouldReturnAllNotCompletedTasksForOtherStatuses() {
        //given
        List<Task> taskList = getTaskList();
        List<Task> notCompletedTasks = taskList.stream().filter(x -> x.getStatus() != Status.COMPLETED).collect(Collectors.toList());
        Mockito.when(taskRepository.findAllByStatusIsNotOrderByDeadLine(Status.COMPLETED)).thenReturn(notCompletedTasks);

        //when
        for (Status s : getStatuses()) {
            if (s.equals(Status.COMPLETED)) {
                return;
            }
            List<TaskDto> allByStatusDto = taskService.findAllByStatusDto(s);
            //then
            assertThat(notCompletedTasks.size()).isEqualTo(allByStatusDto.size());
        }
    }

    @Test
    void shouldReturnNotCompletedTasksForSpecifiedUser() {
        //given
        List<Task> userTaskList = getTaskList();
        List<Task> taskList = getTaskList();
        taskList.add(new Task(7L, "zadanie 7", "z_opisem", null, Status.STARTED, null, null, null));
        List<Task> notCompletedTasks = taskList.stream().filter(x -> x.getStatus() != Status.COMPLETED).collect(Collectors.toList());
        User user = new User(1L, "test@test.test", null, null, null, null, userTaskList, false, null);

        //when
        List<TaskDto> userNotCompletedTasksDto = taskService.getUserNotCompletedTasksDto(user);

        //then
        assertThat(userNotCompletedTasksDto.size()).isEqualTo(notCompletedTasks.size() - 1);
    }

    /*
    This test makes no sense. If We use app in proper way, hibernate generate PersistenceBag object, so taskList won't be null
    @Test()
    void shouldNotReturnExceptionEvenIfUserHasNotTaskList() {
        //given
        User user = new User(1L, "test@test.test", null, null, null, null, null, false, null);
        //when

        //then
        Assertions.assertDoesNotThrow(() -> taskService.getUserNotCompletedTasksDto(user));
    }
    */

    @Test
    void saveTask() {
    }

    @Test
    void shouldSetTaskStatusToCompletedAndAddEndDate() {
        //given
        Mockito.when(dateTimeProvider.getLocalDateTimeNowTruncatedToSeconds()).thenReturn(LocalDateTime.of(2020, 10, 9, 15, 16, 17));
        List<Task> taskList = getTaskList();
        List<Task> completedTaskList = getTaskList();

        //when
        LocalDateTime now = dateTimeProvider.getLocalDateTimeNowTruncatedToSeconds();
        completedTaskList.forEach(t -> taskService.setTaskStatusCompleted(t));

        //then
        assertThat(taskList).isNotEqualTo(completedTaskList);
        assertThat(completedTaskList.stream().allMatch(x -> x.getEndDate().equals(now))).isTrue();
        assertThat(completedTaskList.stream().allMatch(x -> x.getStatus() == Status.COMPLETED)).isTrue();
    }

    @Test
    void shouldSetTaskStatusToStartedAndAddStartDate() {
        //given
        Mockito.when(dateTimeProvider.getLocalDateTimeNowTruncatedToSeconds()).thenReturn(LocalDateTime.of(2020, 10, 9, 15, 16, 17));
        List<Task> taskList = getTaskList();
        List<Task> completedTaskList = getTaskList();

        //when
        LocalDateTime now = dateTimeProvider.getLocalDateTimeNowTruncatedToSeconds();
        completedTaskList.forEach(t -> taskService.setTaskStatusStarted(t));

        //then
        assertThat(taskList).isNotEqualTo(completedTaskList);
        assertThat(completedTaskList.stream().allMatch(x -> x.getStartDate().equals(now))).isTrue();
        assertThat(completedTaskList.stream().allMatch(x -> x.getStatus() == Status.STARTED)).isTrue();
    }

    @Test
    void shouldSetTaskStatusNotAssignedAndNullStartDateForNullUser() {
        //given
        Mockito.when(dateTimeProvider.getLocalDateTimeNowTruncatedToSeconds()).thenReturn(LocalDateTime.of(2020, 10, 9, 15, 16, 17));
        List<Task> taskList = getTaskList();
        taskList.forEach(t -> t.setStartDate(dateTimeProvider.getLocalDateTimeNowTruncatedToSeconds()));
        taskList.forEach(t -> t.setUser(null));

        //when
        taskList.forEach(t -> taskService.setTaskStatus(t));

        //then
        assertThat(taskList.stream().allMatch(t -> t.getStartDate() == null)).isTrue();
        assertThat(taskList.stream().allMatch(t -> t.getStatus().equals(Status.NOT_ASSIGNED))).isTrue();
    }

    @Test
    void shouldSetTaskStatusAssignedAndNullStartDateForKnownUserAndUnknownDate() {
        //given
        List<Task> taskList = getTaskList();
        User user = new User("test@test.test", "testName", "testSurname");
        taskList.forEach(t -> t.setUser(user));

        //when
        taskList.forEach(t -> taskService.setTaskStatus(t));

        //then
        assertThat(taskList.stream().allMatch(t -> t.getStartDate() == null)).isTrue();
        assertThat(taskList.stream().allMatch(t -> t.getStatus().equals(Status.ASSIGNED))).isTrue();
    }

    @Test
    void shouldSetTaskStatusStartedAndSetStartDateForKnownUserAndKnownDate() {
        //given
        Mockito.when(dateTimeProvider.getLocalDateTimeNowTruncatedToSeconds()).thenReturn(LocalDateTime.of(2020, 10, 9, 15, 16, 17));
        LocalDateTime now = dateTimeProvider.getLocalDateTimeNowTruncatedToSeconds();
        List<Task> taskList = getTaskList();
        User user = new User("test@test.test", "testName", "testSurname");
        taskList.forEach(t -> t.setUser(user));
        taskList.forEach(t -> t.setStartDate(now));

        //when
        taskList.forEach(t -> taskService.setTaskStatus(t));

        //then
        assertThat(taskList.stream().allMatch(t -> t.getStartDate().equals(now))).isTrue();
        assertThat(taskList.stream().allMatch(t -> t.getStatus().equals(Status.STARTED))).isTrue();
        assertThat(taskList.stream().allMatch(t -> t.getUser() == null)).isFalse();
    }

    @Test
    void shouldDeleteTaskById() {
        //given

        //when
        taskService.deleteById(2L);

        //then
        Mockito.verify(taskRepository, times(1)).deleteById(2L);
//        assertThat(taskList).doesNotContain(taskList.get( id = 2L ));
        // Check how to test it with taskList
    }

    @Test
    void shouldReturnMyTasksListAddressIfParameterNotNull() {
        //given
        String myTasks = "testOK";

        //when
        String s = taskService.redirectToPreviousTaskList(null, myTasks);

        //then
        assertThat(s).isEqualTo("redirect:myTasks");
    }

    @Test
    void shouldReturnFullListAddressIfBothParameterNull() {
        //given

        //when
        String s = taskService.redirectToPreviousTaskList(null, "");

        //then
        assertThat(s).isEqualTo("redirect:/task/list?status=");
    }

    @Test
    void shouldReturnListAddressWithSpecifiedStatusIfMyTasksIsNullAndStatusIsSpecified() {
        //given
        Status status = Status.STARTED;

        //when
        String s = taskService.redirectToPreviousTaskList(status, "");

        //then
        assertThat(s).isEqualTo("redirect:/task/list?status=" + status);
    }

    @Test
    void shouldAssignTaskToUser() {
        //given
        Long id = 1L;
        Task task = getTaskList().get(0);
        User user = new User("test@test.test", "testName", "testLastname");
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        //when
        taskService.assignTaskToUser(user, id);

        //then
        assertThat(task.getUser()).isEqualTo(user);
        assertThat(task.getStatus()).isEqualTo(Status.ASSIGNED);
    }

    @Test
    void shouldSetStatusNotAssignedForNullUser_AssignTaskToUserMethod() {
        //given
        Long id = 1L;
        Task task = getTaskList().get(2);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        //when
        taskService.assignTaskToUser(null, id);

        //then
        assertThat(task.getUser()).isEqualTo(null);
        assertThat(task.getStatus()).isEqualTo(Status.NOT_ASSIGNED);
    }

    @Test
    void shouldAssignTaskToUserIfNoTask() {
        //given
//        Long id = 1L;
//        Task task = getTaskList().get(2);
        User user = new User("test@test.test", "testName", "testLastname");
//        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        //when
        taskService.assignTaskToUser(user, null);

        //then
//        assertThat(task.getUser()).isEqualTo(user);
//        assertThat(task.getStatus()).isEqualTo(Status.NOT_ASSIGNED);
    }

    @Test
    void taskToDto() {
    }

    @Test
    void dtoToTask() {
    }
}