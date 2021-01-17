package filesync;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;

public class Synchronizer {
    public Synchronizer(){

    }
    public Synchronizer(FileSystem local,List<FileSystem>others){

    }
   public void notifies(List<Action> actions){
       System.out.println(actions.size());
        for(Action event:actions) {
            System.out.format(event.toString()+"\n");
        }
   }
}
