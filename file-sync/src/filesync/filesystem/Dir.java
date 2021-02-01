package filesync.filesystem;

import filesync.action.BaseAction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Dir extends Node {
    List<Node> children;
    public Dir(Node parent, java.lang.String name, java.lang.String base) {
        super(parent,name,base);
        children=new ArrayList<>();
    }
    public Dir(Node parent, java.lang.String name, java.lang.String base,boolean dirty){
        super(parent,name,base);
        children=new ArrayList<>();
        try {
            List<Path> l = Files.walk(getPath(), 1).collect(Collectors.toList());
            for (int i = 1; i < l.size(); i++) {
                //BasicFileAttributes att = Files.readAttributes(l.get(i), BasicFileAttributes.class);
                //java.lang.String k = att.fileKey().toString();
                //int key = Integer.parseInt(k.substring(k.indexOf("ino=") + 4, k.lastIndexOf(")")));
                // Node n = child(key);
                  Node  n = (Files.isDirectory(l.get(i)) ? new Dir(this, l.get(i).toString(), base,true) :
                            new File(this, l.get(i).toString(), getBase()));
                    notif(new BaseAction(n.getPathStr().substring(getBase().length()), "ADD"));
                    addchild(n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeChild(Node n) {
        children=children.stream().filter(new Predicate<Node>() {
            @Override
            public boolean test(Node node) {
                return !node.name.equals(n.getPathStr());
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void addchild(Node e){
        children.add(e);
    }
    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean isDir() {
        return true;
    }

    @Override
    public void update(java.lang.String txt) {

    }

    @Override
    public void removesAll() {
        children.clear();
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

}
