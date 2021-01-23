package filesync.filesystem;

import filesync.Service;
import filesync.Utils;
import filesync.action.BaseAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class LocalFileSystem implements FileSystem{
    protected Node root;
   // protected Service service;
    protected java.lang.String basePath;
    //protected Thread service;
    public LocalFileSystem(java.lang.String base, java.lang.String rootPath)
    {
        basePath=base;

        root=new Dir(null,base+rootPath,base);
        //this.service= new Thread(this.service);//.start();
        //this.service.start();
        Utils.walk(this.root);

    }
    public java.lang.String getBase(){ return basePath;}

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public Node getParent(java.lang.String path) {
        return null;
    }

    @Override
    public List<String> getChildren(String path) {

        Path p1= Paths.get(basePath+path);
        try {

           return Files.walk(p1,1).map(p -> p.toString().substring(getBase().length())).skip(1).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getAncestors(String path) {
        return null;
    }

    @Override
    public java.lang.String getAbsolutePath(java.lang.String relativePath) {
        return getBase()+relativePath;
    }

    @Override
    public java.lang.String getRelativePath(java.lang.String absolutePath) {
        return null;
    }

    @Override
    public FileSystem getReference() {
        return null;
    }

    @Override
    public File createDirectory(Path path) {
        path.toFile().mkdirs();
        return null;
    }
    @Override
    public void fileCopy(File input, File output) throws Exception {
       // output.mkdirs();
        if(!output.exists())
           Files.copy(input.toPath(),output.toPath());
    }

    @Override
    public List<BaseAction> computerDirty() {
        Stack<Node> piles=new Stack<>();
        piles.push(this.root);
        while (!piles.isEmpty()){
            Node cur=piles.pop();
            try {
                cur.computeDirty();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            for(Node n:cur.getChildren())
                piles.push(n);
        }
            return root.getActions();
    }

    @Override
    public void replace(String src, FileSystem fs, String dst) {
        System.out.println("replace:"+src+"->"+dst);
        Node node=   root.find(dst);
        node.setName(Paths.get(src).getFileName().toString());
    }

    @Override
    public void remove(String absolutePathTargetFS, FileSystem src) {
        System.out.println("remove : "+absolutePathTargetFS);
    }

    @Override
    public void rename(String absolutePathSourceFS, FileSystem Source, String absolutePathTargetFS) {

        System.out.println("rename:"+absolutePathSourceFS+"->"+absolutePathTargetFS);
    }

    @Override
    public void createFile(String absolutePathTargetFS) {
        System.out.println("createFile:"+absolutePathTargetFS);
    }
}
