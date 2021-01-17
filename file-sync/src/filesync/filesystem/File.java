package filesync.filesystem;

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
    public void removeChild(Node node) {

    }
}
