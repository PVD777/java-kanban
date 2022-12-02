public class SubTask extends Task {
    protected int epicId;

    protected SubTask(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "uniqID=" + getUniqId() +
                ", epicID='" + epicId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
