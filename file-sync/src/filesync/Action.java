package filesync;

import java.util.Date;

public class Action implements Comparable{
    public String key;
    public String action;
    public Long timestamp;

    public Action(String key, String action, Long timestamp) {
        this.key = key;
        this.action = action;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Object o) {
        Action a=(Action) o;
        return (int)(this.timestamp-a.timestamp);
    }

    @Override
    public String toString() {
        return "Action{" +
                "key='" + key + '\'' +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
