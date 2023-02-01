package Test;

import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setManager() {
        taskManager = new InMemoryTaskManager();
    }


}