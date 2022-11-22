public class Task {
    private int uniqID;
    String name;
    String description;
    private String status;
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getUniqID() {
        return uniqID;
    }

    public String getStatus() {
        return status;
    }

    public void setUniqID(int uniqID) {
        this.uniqID = uniqID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "uniqID=" + uniqID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

