package filesync.action;

import java.util.Objects;

public  class  BaseAction implements Action,Comparable{

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

    @Override
    public int compareTo(Object o) {
        BaseAction ba=(BaseAction) o;
        return -this.getFilename().compareTo(ba.getFilename());
    }

    protected String filename;
    protected String action;


    public BaseAction(String filename, String action) {
        this.filename = filename;
        this.action = action;
;
    }



    @Override
    protected Action clone() throws CloneNotSupportedException {
        Action o = null;
        try {

            o = (Action) super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        return o;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseAction that = (BaseAction) o;
        return Objects.equals(filename, that.getFilename());
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }

    @Override
    public String toString() {
        return "{" +
                "filename,'" + filename + '\'' +
                ", action,'" + action + '\'' +
                '}';
    }
}