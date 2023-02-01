package Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import service.FileBackedTasksManager;
import service.InMemoryTaskManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;


class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    String fileName = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "Test List.csv";


    @BeforeEach
    public void setManager() {
        try {
            taskManager = FileBackedTasksManager.loadFromFile(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void fileErase() {
        try {
            PrintWriter pw = new PrintWriter(fileName);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}