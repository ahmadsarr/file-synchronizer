package filesync;

import java.util.List;

public class File extends Node{
    public File(Node parent,String name) {
        super(parent,name);
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public boolean isDir() {
        return false;
    }

    @Override
    public void update(String txt) {

    }

    @Override
    public List<Node> getChildren() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean isdity() {
        return false;
    }
}
