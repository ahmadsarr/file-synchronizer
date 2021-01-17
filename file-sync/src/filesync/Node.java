package filesync;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Node {
    protected Path path;
    protected String name;
    protected Node parent;

    public BasicFileAttributes getAtrributes() {
        return atrributes;
    }

    protected BasicFileAttributes atrributes;
    protected ArrayList<Action>actions;

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

      }
      public Node child(String path){
          List<Node> chdren=getChildren();
          for(Node n:chdren)
              if(n.getPathStr().equals(path))
                  return n;
          return null;
      }
      public abstract void removeChild(Node node);

      public Node child(int inode){
          List<Node> chdren=getChildren();
          for(Node n:chdren)
          {
              String k=n.getAtrributes().fileKey().toString();
              int key =Integer.parseInt(k.substring(k.indexOf("ino=")+4,k.lastIndexOf(")")));
              if(key==inode)
                  return n;
          }
          return null;
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
    public  boolean isdity() throws Exception {
        this.actions.clear();
        Path p=Paths.get(getPathStr());
        if(!Files.exists(p)) {
            actions.add(new Action(getPathStr(), (isDir()?"DIR_":"FILE_")+"DELETED", System.currentTimeMillis()));
            parent.removeChild(this);


            return true;
        }
        BasicFileAttributes attr=Files.readAttributes(p,BasicFileAttributes.class);
        if(!attr.lastModifiedTime().equals(this.atrributes.lastModifiedTime())) {
            if(isFile()) {
                actions.add(new Action(getPathStr(), "FILE_UPDATE", System.currentTimeMillis()));

            }else{
              List<Path>l=Files.walk(path,1).collect(Collectors.toList());
              if(l.size()>getChildren().size()) //child add to that dir
              {
                  for(int i=1;i<l.size();i++)
                  {
                      BasicFileAttributes att=Files.readAttributes(l.get(i),BasicFileAttributes.class);
                      String k=att.fileKey().toString();
                      int key =Integer.parseInt(k.substring(k.indexOf("ino=")+4,k.lastIndexOf(")")));
                      Node n=child(key);
                      if(n==null){//le fichier ajout
                          n=(Files.isDirectory(l.get(i))?new Dir(this,l.get(i).toString()):
                                  new File(this,l.get(i).toString()));
                          addchild(n);
                          actions.add(new Action(n.getPathStr(), "FILE_ADD", System.currentTimeMillis()));
                      }else if(child(l.get(i).toString())==null){//le fichier a ete renommé
                          actions.add(new Action(n.getPathStr(), "FILE_RENAME", System.currentTimeMillis()));
                          n.rename(l.get(i).toString());
                      }
                  }
              }else{//si un enfant a ete supprimé
                  //nop
              }

            }
            this.atrributes=attr;
        }

        return actions.size()>0;

    }
    public String getPathStr(){

            return name;
        //return parent.getPathStr()+""+ File.separator+""+name;
    }
    public Path getPath(){return path;}
    public void addchild(Node e){}
    public List<Action> getActions(){ return actions;}
}
