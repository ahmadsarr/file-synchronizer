package filesync.filesystem;

public class File extends Node {
    public File(Node parent, java.lang.String name, java.lang.String base) {
        super(parent,name,base);
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
    public void update(java.lang.String txt) {

    }

    @Override
    public void removeChild(Node node) {

    }
}
