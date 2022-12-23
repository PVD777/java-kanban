package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, EpicTask task) {
        super(name, description);
        epicId = task.getId();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }




    @Override
    public String toString() {
        return "TasksType.SubTask{" +
                "uniqID=" + getId() +
                ", epicID='" + epicId + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
