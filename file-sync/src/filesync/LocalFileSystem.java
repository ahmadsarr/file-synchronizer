package filesync;

import java.io.File;
import java.util.List;

public class LocalFileSystem implements FileSystem{
    protected Node root;
    protected Service service;
    protected Synchronizer synchronizer;
    public LocalFileSystem(String rootPath)
    {
        root=new Dir(null,rootPath);
        this.synchronizer=new Synchronizer();
        this.service=new Service(root,synchronizer);
        this.service.run();

    }
    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public Node getParent(String path) {
        return null;
    }

    @Override
    public List<Node> getChildren(String path) {
        return null;
    }

    @Override
    public List<Node> getAncestors(String path) {
        return null;
    }

    @Override
    public String getAbsolutePath(String relativePath) {
        return null;
    }

    @Override
    public String getRelativePath(String absolutePath) {
        return null;
    }

    @Override
    public FileSystem getReference() {
        return null;
    }

    @Override
    public File createDirectory(String path) {
        return null;
    }

    @Override
    public void fileCopy(File input, File output) throws Exception {

    }

}
