package filesync.filesystem;

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
