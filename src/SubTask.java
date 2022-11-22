public class SubTask extends Task {
    int epicID;

    public SubTask(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "uniqID=" + getUniqID() +
                ", epicID='" + epicID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
