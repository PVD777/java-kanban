import java.util.HashMap;

public class EpicTask extends Task {
        HashMap<Integer, SubTask> subTasks = new HashMap<>();

     EpicTask(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "uniqID=" + getUniqId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", subTasks='" + subTasks + '\'' +
                '}';
    }
}
