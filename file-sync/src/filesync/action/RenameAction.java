package filesync.action;

public class RenameAction extends BaseAction{
    public String  newFileName;
    public RenameAction(String oldfilename,String newFileName) {
        super(oldfilename, "RENAME");
        this.newFileName=newFileName;
    }

    public String getNewFileName() {
        return newFileName;
    }

    @Override
    public String toString() {
        return "{" +
                "filename:'" + filename + '\'' +
                ", action:'" + action + '\'' +

                ", newFileName:'" + newFileName + '\'' +
                '}';
    }
}
