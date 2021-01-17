package filesync;

import filesync.action.Action;
import filesync.filesystem.FileSystem;

import java.util.List;

public class Synchronizer {
    protected FileSystem localfs;
    protected FileSystem remotefs;
    public Synchronizer(){

    }
    public Synchronizer(FileSystem local, List<FileSystem>others){
        this.localfs=local;
    }
    public Synchronizer(FileSystem local){
        this.localfs=local;
    }
    public void subscribe(FileSystem fs)
    {
        remotefs=fs;

    }
   public void notifies(){
        List<Action>dirtylfs=localfs.computerDirty();
        List<Action>dirtyrfs=remotefs.computerDirty();
   }
}
