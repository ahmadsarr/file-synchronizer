package filesync.action;

public  class  BaseAction implements Action{

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    protected String filename;
    protected String action;
    protected Long timestamp;

    public BaseAction(String filename, String action, Long timestamp) {
        this.filename = filename;
        this.action = action;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Object o) {
        BaseAction a=(BaseAction) o;
        return (int)(this.timestamp-a.timestamp);
    }

    @Override
    public String toString() {
        return "Action{" +
                "filename,'" + filename + '\'' +
                ", action,'" + action + '\'' +
                ", timestamp," + timestamp +
                '}';
    }
}