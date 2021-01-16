package filesync;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;

public class Synchronizer {
    public Synchronizer(){

    }
    public Synchronizer(FileSystem local,List<FileSystem>others){

    }
   public void notifies(List<WatchEvent<?>> actions){

        for(WatchEvent<?> event:actions) {
            final Path name = (Path) event.context();
            System.out.format(event.kind() + " " + "%s\n", name);
        }
   }
}
