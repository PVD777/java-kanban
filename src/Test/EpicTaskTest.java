package Test;

import model.EpicTask;
import model.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;
import service.TaskStatus;

import java.io.IOException;
import java.time.Duration;

class EpicTaskTest {
    TaskManager manager;

    @BeforeEach
    public void setManager() throws IOException, InterruptedException { manager = Managers.getDefault();}

    @Test
    public void shouldBeEpicStatusNewIfEpicWithoutSub() throws IOException {
        EpicTask epic = new EpicTask("Эпичное название", "Эпичное описание описание");
        manager.createNewEpicTask(epic);
        TaskStatus epicStatus = epic.getStatus();
        Assertions.assertEquals(TaskStatus.NEW, epicStatus);
    }

    @Test
    public void shouldBeEpicStatusNewIfEpicWithSomeNewSub() throws IOException {
        EpicTask epic = new EpicTask("Эпичное название", "Эпичное описание описание");
        manager.createNewEpicTask(epic);
        SubTask sub1 = new SubTask("Название подзадачи1", "Описание подзадачи1", epic);
        SubTask sub2 = new SubTask("Название подзадачи2", "Описание подзадачи2", epic);
        SubTask sub3 = new SubTask("Название подзадачи3", "Описание подзадачи3", epic);
        manager.createNewSubTask(sub1);
        manager.createNewSubTask(sub2);
        manager.createNewSubTask(sub3);
        TaskStatus epicStatus = epic.getStatus();
        Assertions.assertEquals(TaskStatus.NEW, epicStatus);
    }

    @Test
    public void shouldBeEpicStatusDoneIfEpicWithSomeDoneSub() throws IOException {
        EpicTask epic = new EpicTask("Эпичное название", "Эпичное описание описание");
        manager.createNewEpicTask(epic);
        SubTask sub1 = new SubTask("Название подзадачи1", "Описание подзадачи1", epic);
        SubTask sub2 = new SubTask("Название подзадачи2", "Описание подзадачи2", epic);
        SubTask sub3 = new SubTask("Название подзадачи3", "Описание подзадачи3", epic);
        manager.createNewSubTask(sub1);
        manager.createNewSubTask(sub2);
        manager.createNewSubTask(sub3);
        sub1.setStatus(TaskStatus.DONE);
        sub2.setStatus(TaskStatus.DONE);
        sub3.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub1);
        manager.updateSubTask(sub2);
        manager.updateSubTask(sub3);
        TaskStatus epicStatus = epic.getStatus();
        Assertions.assertEquals(TaskStatus.DONE, epicStatus);
    }


    @Test
    public void shouldBeEpicStatusInProgressIfEpicWithSomeNewAndDoneSub() throws IOException {
        EpicTask epic = new EpicTask("Эпичное название", "Эпичное описание описание");
        manager.createNewEpicTask(epic);
        SubTask sub1 = new SubTask("Название подзадачи1", "Описание подзадачи1", epic);
        SubTask sub2 = new SubTask("Название подзадачи2", "Описание подзадачи2", epic);
        SubTask sub3 = new SubTask("Название подзадачи3", "Описание подзадачи3", epic);
        manager.createNewSubTask(sub1);
        manager.createNewSubTask(sub2);
        manager.createNewSubTask(sub3);
        sub1.setStatus(TaskStatus.DONE);
        sub2.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub1);
        manager.updateSubTask(sub2);
        TaskStatus epicStatus = epic.getStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epicStatus);
    }

    @Test
    public void shouldBeEpicStatusInProgressIfEpicWithSomeInProgressSub() throws IOException {
        EpicTask epic = new EpicTask("Эпичное название", "Эпичное описание описание");
        manager.createNewEpicTask(epic);
        SubTask sub1 = new SubTask("Название подзадачи1", "Описание подзадачи1", epic);
        SubTask sub2 = new SubTask("Название подзадачи2", "Описание подзадачи2", epic);
        SubTask sub3 = new SubTask("Название подзадачи3", "Описание подзадачи3", epic);
        manager.createNewSubTask(sub1);
        manager.createNewSubTask(sub2);
        manager.createNewSubTask(sub3);
        sub2.setStatus(TaskStatus.IN_PROGRESS);
        sub3.setStatus(TaskStatus.DONE);
        manager.updateSubTask(sub1);
        manager.updateSubTask(sub2);
        manager.updateSubTask(sub3);
        TaskStatus epicStatus = epic.getStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epicStatus);
    }
    @Test
    public void shouldBeEpicDurationZeroWithOutSub() throws IOException {
        EpicTask epic = new EpicTask("Эпичное название", "Эпичное описание описание");
        manager.createNewEpicTask(epic);
        Assertions.assertEquals(null, epic.getTaskDuration());
    }

    @Test
    public void shouldBeEpicDurationHourWith3Sub() throws IOException {
        EpicTask epic = new EpicTask("Эпичное название", "Эпичное описание описание");
        manager.createNewEpicTask(epic);
        SubTask sub1 = new SubTask("Название подзадачи1", "Описание подзадачи1", "01.02.2023|10:00", 20L, epic);
        SubTask sub2 = new SubTask("Название подзадачи2", "Описание подзадачи2", "01.02.2023|11:00", 20L, epic);
        SubTask sub3 = new SubTask("Название подзадачи3", "Описание подзадачи3", "01.02.2023|12:00", 20L, epic);
        manager.createNewSubTask(sub1);
        manager.createNewSubTask(sub2);
        manager.createNewSubTask(sub3);
        Assertions.assertEquals(Duration.ofMinutes(60), epic.getTaskDuration());
    }



}