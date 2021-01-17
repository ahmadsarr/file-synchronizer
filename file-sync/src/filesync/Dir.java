package filesync;

import java.util.ArrayList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Dir extends  Node{
    List<Node> children;
    public Dir(Node parent,String name) {
        super(parent,name);
        children=new ArrayList<>();
    }

    @Override
    public void removeChild(Node n) {
        children=children.stream().filter(new Predicate<Node>() {
            @Override
            public boolean test(Node node) {
                return !node.getPathStr().equals(n.getPathStr());
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
    public void update(String txt) {

    }

    @Override
    public List<Node> getChildren() {
        return children;
    }


}
