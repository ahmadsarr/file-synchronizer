package filesync.action;

public class RenameAction extends BaseAction{
    public String  newFileName;
    public RenameAction(String oldfilename,String newFileName, Long timestamp) {
        super(oldfilename, "RENAME", timestamp);
        this.newFileName=newFileName;
    }

    @Override
    public String toString() {
        return "RenameAction{" +
                "filename:'" + filename + '\'' +
                ", action:'" + action + '\'' +
                ", timestamp:" + timestamp +
                ", newFileName:'" + newFileName + '\'' +
                '}';
    }
}
