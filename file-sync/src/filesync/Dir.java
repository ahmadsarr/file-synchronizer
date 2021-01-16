package filesync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Dir extends  Node{
    List<Node> children;
    public Dir(Node parent,String name) {
        super(parent,name);
        children=new ArrayList<>();
    }
    public void addchildren(Node e){
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
