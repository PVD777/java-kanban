public class Task {
    private int uniqId;
    String name;
    String description;
    //private String status;
    private TaskStatus status;
    protected Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getUniqId() {
        return uniqId;
    }


    public void setUniqId(int uniqID) {
        this.uniqId = uniqID;
    }



    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "Task{" +
                "uniqID=" + uniqId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

