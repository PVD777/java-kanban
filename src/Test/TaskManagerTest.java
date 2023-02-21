package Test;

import model.EpicTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.TaskManager;
import service.TaskStatus;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    Task task1;
    Task task2;
    EpicTask epic1;
    EpicTask epic2;
    SubTask sub1;
    SubTask sub2;

    public void tasksCreating() {
        try {
            task1 = new Task("Просто задача", "Просто описание");
            task2 = new Task("Просто задача2", "Просто описание2");
            taskManager.createNewTask(task1);
            taskManager.createNewTask(task2);
            epic1 = new EpicTask("Эпичная задача", "Эпичное описание");
            epic2 = new EpicTask("Эпичная задача2", "Эпичное описание2");
            taskManager.createNewEpicTask(epic1);
            taskManager.createNewEpicTask(epic2);
            sub1 = new SubTask("Подзадачка", "Подописинце", epic1);
            sub2 = new SubTask("Подзадачка2", "Подописинце2", epic1);
            taskManager.createNewSubTask(sub1);
            taskManager.createNewSubTask(sub2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void shouldFindAllTask() {
        tasksCreating();
        List<Task> tasks = taskManager.findAllTasks();
        Assertions.assertEquals(2, tasks.size());
        Assertions.assertEquals(tasks, List.of(task1, task2));
    }

    @Test
    public void shouldFindAllEpicTask() {
        tasksCreating();
        List<EpicTask> epicTasks = taskManager.findAllEpicTasks();
        Assertions.assertEquals(2, epicTasks.size());
        Assertions.assertEquals(epicTasks, List.of(epic1, epic2));
    }

    @Test
    public void shouldFindAllSubTask() {
        tasksCreating();
        List<SubTask> subTasks = taskManager.findAllSubTasks();
        Assertions.assertEquals(2, subTasks.size());
        Assertions.assertEquals(subTasks, List.of(sub1, sub2));
    }

    @Test
    public void shouldFindAllSubOfEpic() {
        tasksCreating();
        List<SubTask> subTasksOfEpic1 = taskManager.findTasksOfEpic(epic1);
        List<SubTask> subTasksOfEpic2 = taskManager.findTasksOfEpic(epic2);
        Assertions.assertEquals(2, subTasksOfEpic1.size());
        assertEquals(subTasksOfEpic1, List.of(sub1, sub2));
        assertTrue(subTasksOfEpic2.isEmpty());
    }


    @Test
    public void shouldClearTaskList() throws IOException {
        tasksCreating();
        taskManager.clearTasks();
        assertTrue(taskManager.findAllTasks().isEmpty());
    }

    @Test
    public void shouldClearEpicTaskList() throws IOException {
        tasksCreating();
        taskManager.clearEpicTasks();
        assertTrue(taskManager.findAllEpicTasks().isEmpty());
    }

    @Test
    public void shouldClearSubTaskList() throws IOException {
        tasksCreating();
        taskManager.clearSubTasks();
        assertTrue(taskManager.findAllSubTasks().isEmpty());
    }

    @Test
    public void shouldGetTask() throws IOException {
        tasksCreating();
        Task returnedTask = taskManager.getTask(1);
        assertEquals(returnedTask, task1);
    }

    @Test
    public void shouldGetEpic() throws IOException {
        tasksCreating();
        EpicTask returnedTask = taskManager.getEpicTask(3);
        assertEquals(returnedTask, epic1);
    }

    @Test
    public void shouldGetSub() throws IOException {
        tasksCreating();
        SubTask returnedTask = taskManager.getSubTask(5);
        assertEquals(returnedTask, sub1);
    }

    @Test
    public void shouldUpdateTask() throws IOException {
        tasksCreating();
        task1.setStatus(TaskStatus.DONE);
        task1.setDescription("Блаблабла");
        task1.setName("Блабла");
        taskManager.updateTask(task1);
        assertEquals(TaskStatus.DONE, task1.getStatus());
        assertEquals("Блаблабла", task1.getDescription());
        assertEquals("Блабла", task1.getName());
    }

    @Test
    public void shouldUpdateEpic() throws IOException {
        tasksCreating();
        epic1.setDescription("Блаблабла");
        epic1.setName("Блабла");
        taskManager.updateEpicTask(epic1);
        assertEquals("Блаблабла", epic1.getDescription());
        assertEquals("Блабла", epic1.getName());
    }

    @Test
    public void shouldUpdateSub() {
        tasksCreating();
        sub1.setStatus(TaskStatus.DONE);
        sub1.setDescription("Блаблабла");
        sub1.setName("Блабла");
        assertEquals(TaskStatus.DONE, sub1.getStatus());
        assertEquals("Блаблабла", sub1.getDescription());
        assertEquals("Блабла", sub1.getName());
    }

    @Test
    public void shouldRemoveTask() throws IOException {
        tasksCreating();
        taskManager.removeTask(1);
        List<Task> tasks = taskManager.findAllTasks();
        boolean isHere = tasks.contains(taskManager.getTask(1));
        assertFalse(isHere);
    }

    @Test
    public void shouldRemoveEpic() throws IOException {
        tasksCreating();
        taskManager.removeEpicTask(3);
        List<EpicTask> tasks = taskManager.findAllEpicTasks();
        boolean isHere = tasks.contains(taskManager.getEpicTask(3));
        assertFalse(isHere);
    }

    @Test
    public void shouldRemoveSub() throws IOException {
        tasksCreating();
        taskManager.removeSubTask(5);
        List<SubTask> tasks = taskManager.findAllSubTasks();
        boolean isHere = tasks.contains(taskManager.getSubTask(5));
        assertFalse(isHere);
    }

    @Test
    public void shouldFindAllTaskWithOutTasks() {
        List<Task> tasks = taskManager.findAllTasks();
        Assertions.assertEquals(0, tasks.size());
    }

    @Test
    public void shouldFindAllEpicTaskWithOutEpic() {
        List<EpicTask> epicTasks = taskManager.findAllEpicTasks();
        Assertions.assertEquals(0, epicTasks.size());
    }

    @Test
    public void shouldFindAllSubTaskWithOutSub() {
        List<SubTask> subTasks = taskManager.findAllSubTasks();
        Assertions.assertEquals(0, subTasks.size());
    }

    @Test
    public void shouldClearTaskListWithOutTasks() throws IOException {
        taskManager.clearTasks();
        assertTrue(taskManager.findAllTasks().isEmpty());
    }

    @Test
    public void shouldClearEpicTaskListWithOutEpic() throws IOException {
        taskManager.clearEpicTasks();
        assertTrue(taskManager.findAllEpicTasks().isEmpty());
    }

    @Test
    public void shouldClearSubTaskListWithOutSub() throws IOException {
        taskManager.clearSubTasks();
        assertTrue(taskManager.findAllSubTasks().isEmpty());
    }

    @Test
    public void shouldGetTaskWithOutTasks() throws IOException {
        Task returnedTask = taskManager.getTask(1);
        assertNull(returnedTask);
    }

    @Test
    public void shouldGetEpicWithOutEpic() throws IOException {
        EpicTask returnedTask = taskManager.getEpicTask(3);
        assertNull(returnedTask);
    }

    @Test
    public void shouldGetSubWithOutSub() throws IOException {
        SubTask returnedTask = taskManager.getSubTask(5);
        assertNull(returnedTask);
    }


    @Test
    public void shouldRemoveTaskWithOutTask() throws IOException {
        taskManager.removeTask(1);
        List<Task> tasks = taskManager.findAllTasks();
        boolean isHere = tasks.contains(taskManager.getTask(1));
        assertFalse(isHere);
    }

    @Test
    public void shouldRemoveEpicWithOutEpic() throws IOException {
        taskManager.removeEpicTask(3);
        List<EpicTask> tasks = taskManager.findAllEpicTasks();
        boolean isHere = tasks.contains(taskManager.getEpicTask(3));
        assertFalse(isHere);
    }

    @Test
    public void shouldRemoveSubWithOutSub() throws IOException {
        taskManager.removeSubTask(5);
        List<SubTask> tasks = taskManager.findAllSubTasks();
        boolean isHere = tasks.contains(taskManager.getSubTask(5));
        assertFalse(isHere);
    }

    @Test
    public void shouldGetNullWithTaskWrongId() throws IOException {
        tasksCreating();
        Task returnedTask = taskManager.getTask(3);
        assertNull(returnedTask);
    }

    @Test
    public void shouldGetNullWithEpicWrongId() throws IOException {
        tasksCreating();
        EpicTask returnedTask = taskManager.getEpicTask(5);
        assertNull(returnedTask);
    }

    @Test
    public void shouldGetNullWithSubWrongId() throws IOException {
        tasksCreating();
        SubTask returnedTask = taskManager.getSubTask(1);
        assertNull(returnedTask);
    }


    @Test
    public void everySubShouldHaveEpic() {
        tasksCreating();
        assertNotNull(sub1.getEpicId());
        assertNotNull(sub2.getEpicId());
    }

    @Test
    public void epicShouldBeNew() {
        tasksCreating();
        assertEquals(epic1.getStatus(), TaskStatus.NEW);
        assertEquals(epic2.getStatus(), TaskStatus.NEW);
    }

    @Test
    public void epicShouldBeInProgress() throws IOException {
        tasksCreating();
        sub1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(sub1);
        assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(epic2.getStatus(), TaskStatus.NEW);
    }

    @Test
    public void epicShouldBeDone() throws IOException {
        tasksCreating();
        sub1.setStatus(TaskStatus.DONE);
        sub2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(sub1);
        taskManager.updateSubTask(sub2);
        assertEquals(epic1.getStatus(), TaskStatus.DONE);
        assertEquals(epic2.getStatus(), TaskStatus.NEW);
    }

    @Test
    public void shouldNotCreateTaskWithConflux() throws IOException {

        task1 = new Task("Просто задача", "Просто описание", "01.01.2023|10:00", 120L);
        task2 = new Task("Просто задача2", "Просто описание2", "01.01.2023|11:00", 20L);
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        assertEquals(1, taskManager.findAllTasks().size());


    }

}