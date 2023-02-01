package Test;

import model.EpicTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;

import java.util.List;

public class HistoryManagerTest {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    Task task1;
    EpicTask epic1;
    SubTask sub1;


    public void taskCreating() {
        task1 = new Task("Просто задача", "Просто описание");
        epic1 = new EpicTask("Эпичная задача", "Эпичное описание");
        sub1 = new SubTask("Подзадачка", "Подописинце", epic1);
        task1.setId(1);
        epic1.setId(2);
        sub1.setId(3);
    }

    @Test
    public void shouldAddTasksInHistory() {
        taskCreating();
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1);
        Assertions.assertEquals(historyManager.getHistory(), List.of(task1, epic1, sub1));
    }

    @Test
    public void historyShouldBeEmpty() {
        taskCreating();
        Assertions.assertEquals(historyManager.getHistory().isEmpty(), true);
    }

    @Test
    public void shouldAddTaskAtStart() {
        taskCreating();
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1);
        historyManager.remove(1);
        Assertions.assertEquals(historyManager.getHistory(), List.of(epic1, sub1));
    }

    @Test
    public void shouldAddTaskAtMiddle() {
        taskCreating();
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1);
        historyManager.remove(2);
        Assertions.assertEquals(historyManager.getHistory(), List.of(task1, sub1));
    }

    @Test
    public void shouldAddTaskAtEnd() {
        taskCreating();
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1);
        historyManager.remove(3);
        Assertions.assertEquals(historyManager.getHistory(), List.of(task1, epic1));
    }

    @Test
    public void shouldNotRepeatHistory() {
        taskCreating();
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1);
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1);
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1);
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(sub1);
        historyManager.add(task1);
        historyManager.add(epic1);
        Assertions.assertEquals(historyManager.getHistory(), List.of(sub1, task1, epic1));
    }

}