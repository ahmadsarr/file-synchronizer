package filesync.filesystem;


import filesync.Utils;
import filesync.action.BaseAction;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
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
        if(!Files.exists(p1))
            return new ArrayList<>();
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
    public File createDirectory(String path) {
        System.out.println("create dir:"+path);
        String file=path.substring(path.lastIndexOf("/")+1);
        Node node=root.find(path.substring(0,path.length()-file.length()-1));
            try {
                node.createFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
    @Override
    public void fileCopy(File input, File output) throws Exception {
        Path path=output.toPath();
        String file=path.getFileName().toString();
        Node node=root.find(path.toString().substring(0,path.toString().length()-file.length()-1));
        System.out.println("create dir:"+path);
        try {
            node.replace(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            node.replace(Paths.get(src).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void remove(String path, FileSystem src) {
        Node node=root.find(path);
        System.out.println("remove:"+node.getPath().toString());
        node.remove();
    }
    @Override
    public void rename(String src, FileSystem Source, String target) {

        System.out.println("rename:"+src+"->"+target);
        Node node=root.find(src);
        try {
            node.rename(Paths.get(target).getFileName().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createFile(String path) {
        System.out.println("create file:"+path);
        String file=path.substring(path.lastIndexOf("/")+1);
        Node node=root.find(path.substring(0,path.length()-file.length()-1));
        try {
            node.createFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
