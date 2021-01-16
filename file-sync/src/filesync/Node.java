package filesync;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Node {
    protected Path path;
    protected String name;
    protected Node parent;
    protected BasicFileAttributes atrributes;
    protected List<WatchEvent<?>>actions;
    protected WatchService watchService;
    protected WatchKey key;

      public Node(Node parent,String name) {

          this.name=name;
          this.parent=parent;
          this.actions=new ArrayList<>();
          this.path= Paths.get(getPathStr());
          try {
              this.atrributes = Files.readAttributes(this.path, BasicFileAttributes.class);
          }catch (Exception exception)
          {
              exception.printStackTrace();
          }
          if(isDir())
          try {
              watchService= FileSystems.getDefault().newWatchService();
              this.path.register(this.watchService,StandardWatchEventKinds.ENTRY_CREATE,
                      StandardWatchEventKinds.ENTRY_DELETE,
                      StandardWatchEventKinds.ENTRY_MODIFY);
          } catch (IOException e) {
              e.printStackTrace();
          }

      }

    public abstract boolean isFile();
    public abstract boolean isDir();
    public  void rename(String name)
    {
        this.name=name;
    }
    public abstract void update(String txt);
    public  List<Node> getChildren(){
        return new ArrayList<Node>();
    }
    public  boolean isdity() throws IOException, InterruptedException {
        /*Path p=Paths.get(getPathStr());
        BasicFileAttributes attr=Files.readAttributes(p,BasicFileAttributes.class);
        boolean dirty=false;
        if(!Files.exists(p)) {
            actions.add(new Action(getPathStr(), "deleted", System.currentTimeMillis()));
            return true;
        }
        return false;*/
       key=this.watchService.poll(1000, TimeUnit.MILLISECONDS);


       return key!=null;
    }
    public String getPathStr(){
        if(path==null)
            return name;
        return parent.getPathStr()+""+ File.separator+""+name;
    }
    public Path getPath(){return path;}
    public void addchild(Node e){}
    public List<WatchEvent<?>> getActions(){ return key.pollEvents();}
}
