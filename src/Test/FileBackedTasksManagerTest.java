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

    File testFile = new File(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "Test List.csv");


    @BeforeEach
    public void setManager() {
        try {
            testFile.createNewFile();
            taskManager = FileBackedTasksManager.loadFromFile(testFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void fileErase() {
        try {
            PrintWriter pw = new PrintWriter(testFile.getPath());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}